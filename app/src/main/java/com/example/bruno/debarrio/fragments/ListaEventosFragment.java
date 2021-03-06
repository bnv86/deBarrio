package com.example.bruno.debarrio.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruno.debarrio.Adapters.AdaptadorEventos;
import com.example.bruno.debarrio.HTTP.HttpServices;
import com.example.bruno.debarrio.R;
import com.example.bruno.debarrio.entidades.Evento;
import com.example.bruno.debarrio.entidades.Subject;
import com.example.bruno.debarrio.interfaces.ComunicacionFragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListaEventosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListaEventosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaEventosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    ArrayList<Evento> listaEventos;
    RecyclerView recyclerViewEventos;
    TextView textviewRegresar;
    Activity activity;
    ComunicacionFragments interfaceComunicacionFragments;
    ProgressBar progressBarEventos;
    String ServerURL = "https://momentary-electrode.000webhostapp.com/getEvento.php";

    public ListaEventosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaEventosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaEventosFragment newInstance(String param1, String param2) {
        ListaEventosFragment fragment = new ListaEventosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_lista_eventos, container, false);
        //progressBarEventos = vista.findViewById(R.id.progressBar);

        //textviewRegresar = vista.findViewById(R.id.textview_regresar);
        /*
        textviewRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onBackPressed(); //vuelve al activity anterior
                Intent intentEventos = new Intent(getActivity(), MainTabbedActivity.class);
                getActivity().startActivity(intentEventos);
            }
        });*/

        listaEventos = new ArrayList<>();
        recyclerViewEventos = (RecyclerView) vista.findViewById(R.id.reciclerId);
        recyclerViewEventos.setLayoutManager(new LinearLayoutManager(getContext()));
        llenarlistaEventos();
        //new GetHttpResponse(getContext()).execute();

        //AdaptadorEventos adapter = new AdaptadorEventos(listaEventos);

        //recyclerViewEventos.setAdapter(adapter);
        /*
        adapter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getContext(), "Seleccionó " + listaEventos.get(recyclerViewEventos.getChildAdapterPosition(view)).getNombre(), Toast.LENGTH_SHORT).show();

                interfaceComunicacionFragments.enviarPersonaje(listaEventos.get(recyclerViewEventos.getChildAdapterPosition(view)));
            }
        });*/
        //progressBarEventos.setVisibility(View.GONE);
        //recyclerViewEventos.setVisibility(View.VISIBLE);
        return vista;
    }

    private void llenarlistaEventos() {
        new GetHttpResponse(getContext()).execute();

/*
        listaEventos.add(new Evento("Android1", "ldkjfosdhfjosdhflsdhfljskdhfoksdhfojhsdfjhsdfokhsdlkfjsdljfhlsdhflksdflshdflksjdflksdlfjhsldfh", "aaaaaaaaaaaaaaa",  R.drawable.camera, R.drawable.camera));
        listaEventos.add(new Evento("Android2","jlhsldhfldhflkjkhgfjkljgflkgjflkgjlkfg456465432465465165465465465465465465465465465465465465465", "bbbbbbbbbbbbbbb", R.drawable.camera, R.drawable.camera));
        listaEventos.add(new Evento("Android3", "algo", "lalalalalalalalalalallalalalalalalalaalalalalaaaaaaaaaaaaaaaaaaaalalalalalalalalalalallaallalalalalalalallaa", R.drawable.camera, R.drawable.camera));
*/
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

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            interfaceComunicacionFragments = (ComunicacionFragments) this.activity;
        }

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

    public class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        public Context context;
        String ResultHolder;
        //List<Subject> eventosList;
        public GetHttpResponse(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            HttpServices httpServiceObject = new HttpServices(ServerURL);
            try
            {
                httpServiceObject.ExecutePostRequest();

                if(httpServiceObject.getResponseCode() == 200)
                {
                    ResultHolder = httpServiceObject.getResponse();

                    if(ResultHolder != null)
                    {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(ResultHolder);
                            JSONObject jsonObject;
                            //Evento evento;
                            //Evento evento = new Evento("fecha", "motivo", "descripcion", R.drawable.camera, R.drawable.camera);
                            //listaEventos = new ArrayList<Evento>();
                            //ArrayList<Evento> listaEventos;
                            //eventosList = new ArrayList<Subject>();
                            //setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Contenido.titulos));

                            for(int i=0; i<jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                String usuario = jsonObject.getString("usuario");
                                //nombre.getNombre(nombre) = jsonObject.getString("fecha");
                                //evento.SubjectMotivo = jsonObject.getString("motivo");
                                String id = jsonObject.getString("id");
                                String dec = jsonObject.getString("foto");
                                Bitmap foto = downloadImage(dec);
                                //String usuario = jsonObject.getString("usuario");
                                String fecha = jsonObject.getString("fecha");
                                String latitud = jsonObject.getString("latitud");
                                String longitud = jsonObject.getString("longitud");
                                String motivo = jsonObject.getString("motivo");
                                String comentario = jsonObject.getString("comentario");
                                String estado = jsonObject.getString("estado");
                                Evento evento = new Evento(id.toString(), usuario.toString(), fecha.toString(), latitud.toString(), longitud.toString(), motivo.toString(), comentario.toString(), estado.toString(), foto, foto);//(fecha, "motivo", "descripcion", R.drawable.camera, R.drawable.camera);
                                listaEventos.add(evento);
                            }
                        }
                        catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    Toast.makeText(context, httpServiceObject.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
            if(listaEventos != null) {
                final AdaptadorEventos adapter = new AdaptadorEventos(listaEventos);
                recyclerViewEventos.setAdapter(adapter);
                adapter.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        //Toast.makeText(getContext(), "Seleccionó " + listaEventos.get(recyclerViewEventos.getChildAdapterPosition(view)).getFecha(), Toast.LENGTH_SHORT).show();
                        interfaceComunicacionFragments.enviarPersonaje(listaEventos.get(recyclerViewEventos.getChildAdapterPosition(view)));
                    }
                });
            }
            else{
                Toast.makeText(context, "Sin conexión con el servidor :(", Toast.LENGTH_LONG).show();
            }
            //progressBarEventos.setVisibility(View.GONE);
            //recyclerViewEventos.setVisibility(View.VISIBLE);
        }
    }

    public static Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        InputStream stream = null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;

        try {
            stream = getHttpConnection(url);
            bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
            stream.close();
        }
        catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("downloadImage"+ e1.toString());
        }
        return bitmap;
    }

    // Makes HttpURLConnection and returns InputStream

    public static  InputStream getHttpConnection(String urlString)  throws IOException {

        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("downloadImage" + ex.toString());
        }
        return stream;
    }
}
