package com.se491.chef_ly.activity.create_recipe_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.se491.chef_ly.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FirstFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {

    private String title;

    private EditText recipeTitle;
    private EditText recipeAuthor;
    private EditText recipeImage;
    private EditText recipeDescription;

    private OnFragmentInteractionListener mListener;

    private String user;

    public FirstFragment() {
        // Required empty public constructor
    }

    public static FirstFragment newInstance(String param1, int param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString("title", param1);
        args.putInt("pageNum", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
           // pageNum = getArguments().getInt("pageNum",0);
            user = getArguments().getString("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_first, container, false);
        recipeTitle = (EditText) v.findViewById(R.id.recipeName);
        recipeAuthor =  (EditText) v.findViewById(R.id.recipeAuthor);
        recipeAuthor.setText(user);
        recipeImage = (EditText) v.findViewById(R.id.recipeImage);
        recipeDescription = (EditText) v.findViewById(R.id.recipeDescription);

        TextView fragTitle = (TextView) v.findViewById(R.id.fragTitle);
        fragTitle.setText(title);

        return v;
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public String getRecipeTitle(){
        try{
            return recipeTitle.getText().toString();
        }catch(NullPointerException e){
            return "";
        }
    }
    public String getRecipeAuthor(){
        try{
            return recipeAuthor.getText().toString();
        }catch(NullPointerException e){
            return "";
        }
    }
    public String getRecipeImage(){
        try{
            return  recipeImage.getText().toString();
        }catch(NullPointerException e){
            return "";
        }
    }
    public String getRecipeDescription(){
        try{
            return recipeDescription.getText().toString();
        }catch(NullPointerException e){
            return "";
        }
    }
}
