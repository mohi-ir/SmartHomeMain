package com.example.mohi_pc.myhome;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.GreenDao.model.DaoMaster;
import com.GreenDao.model.DaoSession;
import com.GreenDao.model.Memory;
import com.GreenDao.model.Room;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MemoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MemoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemoryFragment extends ListFragment {

    private OnFragmentInteractionListener mListener;

    private DaoSession ReadDaoSession;
    private List<Memory> memories;

    public MemoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MemoryFragment.
     */
    public static MemoryFragment newInstance() {
        MemoryFragment fragment = new MemoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        //read all memories from DB
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this.getContext(), "home_database", null);
        SQLiteDatabase db = helper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        ReadDaoSession = daoMaster.newSession();
        memories = ReadDaoSession.getMemoryDao().loadAll();
        db.close();
        helper.close();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MemoryAdapter adapter = new MemoryAdapter(getActivity(), R.layout.editable_memory_item, memories);
        setListAdapter(adapter);
        return inflater.inflate(R.layout.fragment_memory, container, false);
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
        public void MemoryOnDataPass(Long id, String name);
    }

    public class MemoryAdapter extends ArrayAdapter<Memory> {

        public MemoryAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public MemoryAdapter(Context context, int resource, List<Memory> memories_list) {
            super(context, resource, memories_list);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.editable_memory_item, null);
            }

            final Memory memory = getItem(position);

            if (memory != null) {

                final EditText et = (EditText) v.findViewById(R.id.editText_memory_fragment_item);

                if (et != null) {
                    et.setText(memory.getName());

                    et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        public void onFocusChange(View v, boolean gainFocus) {

                            if (!gainFocus) {
                                mListener.MemoryOnDataPass(memory.getId(),String.valueOf(et.getText()));
                            }
                        }
                    });

                }
            }
            return v;
        }
    }
}
