package com.spacemadness.lunar.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spacemadness.lunar.console.Terminal;

import spacemadness.com.lunar.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TerminalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TerminalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TerminalFragment extends Fragment implements CommandEditText.OnCommandRunListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final Terminal terminal;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecycleView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TerminalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TerminalFragment newInstance(String param1, String param2)
    {
        TerminalFragment fragment = new TerminalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TerminalFragment()
    {
        terminal = new Terminal();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_terminal, container, false);
        setupUI(view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mListener = (OnFragmentInteractionListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    private void setupUI(View view)
    {
        final CommandEditText commandEditText = (CommandEditText) view.findViewById(R.id.edit_text_command_line);
        commandEditText.setCommandListener(this);

        setClickListener(view, R.id.button_up, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                commandEditText.historyPrev();
            }
        });
        setClickListener(view, R.id.button_down, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                commandEditText.historyNext();
            }
        });
        setClickListener(view, R.id.button_left, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        setClickListener(view, R.id.button_right, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        setClickListener(view, R.id.button_clear, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                commandEditText.clear();
            }
        });
    }

    private void setClickListener(View view, int id, View.OnClickListener listener)
    {
        view.findViewById(id).setOnClickListener(listener);
    }

    @Override
    public void onCommandRun(CommandEditText v, String commandLine)
    {
        terminal.ExecuteCommandLine(commandLine, true);
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
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
