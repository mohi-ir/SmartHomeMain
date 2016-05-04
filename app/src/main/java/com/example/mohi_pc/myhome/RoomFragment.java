package com.example.mohi_pc.myhome;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TextView;

import com.GreenDao.model.DaoMaster;
import com.GreenDao.model.DaoSession;
import com.GreenDao.model.Room;
import android.view.View.OnClickListener;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class RoomFragment extends ListFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CURRENT_ROOM = "currentRoom";

    private int cuRoom = 0;

    private DaoSession ReadDaoSession;

    private OnFragmentInteractionListener mListener;

    private List<Room> rooms;

    public RoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment RoomFragment.
     */
    public static RoomFragment newInstance(int param1) {
        RoomFragment fragment = new RoomFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CURRENT_ROOM, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
           cuRoom = getArguments().getInt(ARG_CURRENT_ROOM);

        }

        //read all rooms from memory
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this.getContext(), "home_database", null);
        SQLiteDatabase db = helper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        ReadDaoSession = daoMaster.newSession();
        rooms = ReadDaoSession.getRoomDao().loadAll();
        db.close();
        helper.close();

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            cuRoom = savedInstanceState.getInt("currentRoom", 0);

        }
        else{
            cuRoom =0;

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_room, container, false);
        RoomAdapter adapter = new RoomAdapter(getActivity(), R.layout.editable_llist_item, rooms);
        setListAdapter(adapter);

        return view;
    }

      // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        public void RoomOnDataPass(Long id, String name);
    }

    public class RoomAdapter extends ArrayAdapter<Room> {

        public RoomAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public RoomAdapter(Context context, int resource, List<Room> rooms_list) {
            super(context, resource, rooms_list);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.editable_llist_item, null);
            }

            final Room room = getItem(position);

            if (room != null) {

                final EditText et = (EditText) v.findViewById(R.id.editText_room_fragment_item);
                Button bt = (Button) v.findViewById(R.id.button_room_item);

                if (et != null) {
                    et.setText(room.getName());

                    et.setOnFocusChangeListener(new OnFocusChangeListener() {
                        public void onFocusChange(View v, boolean gainFocus) {

                            if (!gainFocus) {
                                mListener.RoomOnDataPass(room.getId(),String.valueOf(et.getText()));
                            }
                        }
                    });
                    bt.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            ((View) v.getParent()).setBackgroundColor(Color.rgb(255, 248, 220));
                            //v.setBackgroundColor(Color.rgb(255, 248, 220));
                            for(int j=0;j<parent.getChildCount();j++){
                                if (j!=position){
                                    parent.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                                }
                            }
                            showWallUnits(position);
                        }
                    });
                }
            }
            return v;
        }
    }

    public void showWallUnits(int position){
        cuRoom = position;
        getListView().setItemChecked(position, true);

        WallUnitFragment wallUnits = (WallUnitFragment) getFragmentManager().findFragmentById(R.id.fragment_wallUnit);
        wallUnits = WallUnitFragment.newInstance(rooms.get(position).getId(),0);
        ChannelFragment channels = (ChannelFragment) getFragmentManager().findFragmentById(R.id.fragment_channel);

        getFragmentManager().beginTransaction().replace(R.id.fragment_wallUnit, wallUnits).commit();
        if(channels != null) {
            getFragmentManager().beginTransaction().remove(channels).commit();
        }
    }
}
