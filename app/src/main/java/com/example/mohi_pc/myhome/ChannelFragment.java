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
import com.GreenDao.model.Channel;
import com.GreenDao.model.ChannelDao;
import com.GreenDao.model.WallUnit;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChannelFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChannelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChannelFragment extends ListFragment {

    private static final String ARG_WALLUNIT = "wallunit";

    private Long mWallunit;

    private OnFragmentInteractionListener mListener;

    private DaoSession ReadDaoSession;

    private List<Channel> channels;

    public ChannelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ChannelFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChannelFragment newInstance(Long param1) {
        ChannelFragment fragment = new ChannelFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_WALLUNIT, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWallunit = getArguments().getLong(ARG_WALLUNIT);
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this.getContext(), "home_database", null);
            SQLiteDatabase db = helper.getReadableDatabase();
            DaoMaster daoMaster = new DaoMaster(db);
            ReadDaoSession = daoMaster.newSession();
            ChannelDao channelDao = ReadDaoSession.getChannelDao();

            QueryBuilder qb = channelDao.queryBuilder();
            qb.where(ChannelDao.Properties.WallUnitId.eq(mWallunit));
            channels = qb.list();
            db.close();
            helper.close();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mWallunit!=null) {
            ChannelFragmentAdapter adapter = new ChannelFragmentAdapter(getActivity(), R.layout.editable_channel_item, channels);
            setListAdapter(adapter);
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_channel, container, false);
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
        public void ChannelsOnDataPass(Long id,String name);
    }

    public class ChannelFragmentAdapter extends ArrayAdapter<Channel> {

        public ChannelFragmentAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public ChannelFragmentAdapter(Context context, int resource, List<Channel> channel_list) {
            super(context, resource, channel_list);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.editable_channel_item, null);
            }

            final Channel channel = getItem(position);

            if (channel != null) {
                final EditText et = (EditText) v.findViewById(R.id.editText_channel_item);

                if (et != null) {
                    et.setText(channel.getName());

                    et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        public void onFocusChange(View v, boolean gainFocus) {

                            if (!gainFocus) {
                                mListener.ChannelsOnDataPass(channel.getId(),String.valueOf(et.getText()));
                            }
                        }
                    });
                }
            }
            return v;
        }
    }
}
