package com.example.mohi_pc.myhome;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.greenrobot.dao.query.QueryBuilder;

import com.GreenDao.model.Channel;
import com.GreenDao.model.DaoMaster;
import com.GreenDao.model.DaoSession;
import com.GreenDao.model.Room;
import com.GreenDao.model.WallUnit;
import com.GreenDao.model.WallUnitDao;
import android.widget.ArrayAdapter;

import android.support.v4.app.ListFragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WallUnitFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WallUnitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WallUnitFragment extends ListFragment {

    private static final String ARG_ROOM = "room";
    private static final String ARG_CURRENT_WALLUNIT = "currentWallunit";

    private Long mRoom;
    private int cuWallU = 0;

    private OnFragmentInteractionListener mListener;

    private DaoSession ReadDaoSession;



    private List<WallUnit> wallUnits;

    public WallUnitFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WallUnitFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WallUnitFragment newInstance(Long param1, int param2) {
        WallUnitFragment fragment = new WallUnitFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ROOM, param1);
        args.putInt(ARG_CURRENT_WALLUNIT, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            cuWallU = getArguments().getInt(ARG_CURRENT_WALLUNIT);
            mRoom = getArguments().getLong(ARG_ROOM);

        }

        if(mRoom!= null) {

            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this.getContext(), "home_database", null);
            SQLiteDatabase db = helper.getReadableDatabase();
            DaoMaster daoMaster = new DaoMaster(db);
            ReadDaoSession = daoMaster.newSession();
            WallUnitDao wallUnitDao = ReadDaoSession.getWallUnitDao();

            QueryBuilder qb = wallUnitDao.queryBuilder();
            qb.where(WallUnitDao.Properties.RoomId.eq(mRoom));
            wallUnits = qb.list();
            db.close();
            helper.close();
        }

        if (savedInstanceState != null) {

            // Restore last state for checked position.
            cuWallU = savedInstanceState.getInt("currentWallunit", 0);
        }
        else {

            cuWallU = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


       if(mRoom!=null) {

            WallUnitAdapter adapter = new WallUnitAdapter(getActivity(), R.layout.editable_wallunit_item, wallUnits);
            setListAdapter(adapter);
       }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wall_unit, container, false);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        public void WallUnitsOnDataPass(Long id,String name);
    }

    public Long getShownIndex() {
        return getArguments().getLong("room", 0L);
    }

    public class WallUnitAdapter extends ArrayAdapter<WallUnit> {

        public WallUnitAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public WallUnitAdapter(Context context, int resource, List<WallUnit> wallunit_list) {
            super(context, resource, wallunit_list);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.editable_wallunit_item, null);
            }

            final WallUnit wallUnit = getItem(position);

            if (wallUnit != null) {

                final EditText et = (EditText) v.findViewById(R.id.editText_wallunit_item);
                Button bt = (Button) v.findViewById(R.id.button_wallunit_item);

                if (et != null) {
                    et.setText(wallUnit.getName());

                    bt.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            ((View) v.getParent()).setBackgroundColor(Color.rgb(255, 248, 220));
                           // v.setBackgroundColor(Color.rgb(255, 248, 220));
                            for(int j=0;j<parent.getChildCount();j++){
                                if (j!=position){
                                    parent.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                                }
                            }
                            showChannels(position);
                        }
                    });
                    et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        public void onFocusChange(View v, boolean gainFocus) {

                            if (!gainFocus) {
                                mListener.WallUnitsOnDataPass(wallUnit.getId(),String.valueOf(et.getText()));
                            }
                        }
                    });
                }
            }
            return v;
        }
    }

    public void showChannels(int position){
        cuWallU = position;
        getListView().setItemChecked(position, true);

        ChannelFragment channels = (ChannelFragment) getFragmentManager().findFragmentById(R.id.fragment_channel);
        channels = ChannelFragment.newInstance(wallUnits.get(position).getId());

        getFragmentManager().beginTransaction().replace(R.id.fragment_channel, channels).commit();
    }
}
