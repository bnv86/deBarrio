package com.example.bruno.debarrio.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bruno.debarrio.AddEmailActivity;
import com.example.bruno.debarrio.MainTabbedActivity;
import com.example.bruno.debarrio.PostsDB.PedidoDeEmail;
import com.example.bruno.debarrio.PostsDB.PedidoDeEstado;
import com.example.bruno.debarrio.R;
import com.example.bruno.debarrio.entidades.Evento;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetalleEventoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetalleEventoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetalleEventoFragment extends Fragment{ //implements AdapterView.OnItemSelectedListener    implements View.OnClickListener
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String UPLOAD_URL_ESTADO = "https://momentary-electrode.000webhostapp.com/postEstadoEvento.php";

    private OnFragmentInteractionListener mListener;

    TextView textUsuario, textComentario, textFecha, textCoordenadas, textLatitud, textLongitud; //, textID
    ImageView imagenDetalle;
    EditText etDescrip;
    Button botonActualizarEstado;
    Spinner spinner;
    private String KET_ID = "id";
    private String KEY_ESTADO = "estado";
    private TreeMap<String, String> descrip;

    public DetalleEventoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetalleEventoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetalleEventoFragment newInstance(String param1, String param2) {
        DetalleEventoFragment fragment = new DetalleEventoFragment();
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

    private void actualizarEstado(Spinner spinner) {
        final String estado = spinner.getSelectedItem().toString();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        Intent intent = new Intent(getContext(), MainTabbedActivity.class);
                        getActivity().startActivity(intent);
                        Toast.makeText(getActivity(), "Estado actualizado!", Toast.LENGTH_LONG).show();
                    } else {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                        alertBuilder.setMessage("Error al actualizar estado").setNegativeButton("Reintentar", null).create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PedidoDeEstado pedido = new PedidoDeEstado(estado, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(pedido);
    }

    public void subirEstado(Spinner spinner){
        //Bundle bundleObjeto = getArguments();
        //final String id = e.getId();
        Evento evento2 = null;
        Bundle bundleObjeto2 = getArguments();
        //if (bundleObjeto2 != null) {
        evento2 = (Evento) bundleObjeto2.getSerializable("objeto");
        final String id = evento2.getId();
        //}
        final String estado = spinner.getSelectedItem().toString();
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("sesion",getActivity().getApplication().MODE_PRIVATE);
        //final String usuario = sharedpreferences.getString("username",""); //ME DEVUELVE EL PASSWORD, NO EL USERNAME, PROBLEMA DEL LOGIN??
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Actualizando...","Espere por favor...",false,false); //getActivity()
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL_ESTADO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                        Toast.makeText(getActivity(), "ESTADO ACTUALIZADO!", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        Toast.makeText(getActivity(), "NO SE ACTUALIZÓ...REINTENTE" , Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creación de parámetros
                Map<String,String> params = new Hashtable<String, String>();
                //Agregando de parámetros
                params.put(KET_ID, id);
                params.put(KEY_ESTADO, estado);
                //Parámetros de retorno
                return params;
            }
        };
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 6, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Creación de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(getContext()); //getActivity()
        //Agregar solicitud a la cola
        requestQueue.add(stringRequest);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_detalle_evento, container, false);
        textUsuario = (TextView) vista.findViewById(R.id.detalle_usuario);
        textComentario = vista.findViewById(R.id.detalle_comentario);
        textLatitud = vista.findViewById(R.id.detalle_latitud);
        textLongitud = vista.findViewById(R.id.detalle_longitud);
        imagenDetalle = (ImageView) vista.findViewById(R.id.imagen_detalle);
        //etDescrip = (EditText) vista.findViewById(R.id.etDescription);
        final Spinner spinner = (Spinner) vista.findViewById(R.id.spinner_estado);
        String[] tipos1 = {"Abierto","Resuelto","En curso","Re-abierto"};
        //final String[] tipos2 = {"Abierto","Resuelto","En curso","Re-abierto"};
        //spinner.setAdapter(new ArrayAdapter<String>(this, (inflater.inflate(R.layout.fragment_detalle_evento, container))), tipos));
        spinner.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, tipos1));
        //ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos2);
/*
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.string-estados,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
*/
        //spinner.setAdapter(adapter);
        //spinner.setOnItemSelectedListener(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
            {
                //String name = tipos2[pos];
                //String description = descrip.get(name);
                //etDescrip.setText(description);
                //Toast.makeText(adapterView.getContext(),(String) adapterView.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {    }
        });

        Bundle bundleObjeto = getArguments();
        Evento evento = null;
        //String id;

        if (bundleObjeto != null){
            evento = (Evento) bundleObjeto.getSerializable("objeto");
            imagenDetalle.setImageBitmap(evento.getImagenDesc());
            textUsuario.setText(evento.getUsuarioDesc());
            textComentario.setText(evento.getComentarioDesc());
            textLatitud.setText(evento.getLatitudDesc());
            textLongitud.setText(evento.getLongitudDesc());
            //spinner.setItemAt(evento.getEstado());
            //id = evento.getId();
            spinner.setSelection(((ArrayAdapter<String>)spinner.getAdapter()).getPosition(evento.getEstado()));
            //(((ArrayAdapter<String>)mySpinner.getAdapter()).getPosition(myString));
        }
        botonActualizarEstado = vista.findViewById(R.id.boton_actualizar_estado);
        botonActualizarEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subirEstado(spinner);
            }
        });
        return vista;
    }

    /*
    public void rellenarDescripciones() {
        // Inicializamos la variable.
        descrip = new TreeMap<String, String>();

        // Rellenamos la variable con las descripciones.
        descrip.put(
                "Perro",
                "Mamífero carnívoro de la familia de los cánidos, doméstico, con cuatro patas, un olfato muy fino y de gran diversidad de tamaños, formas y pelajes, que sirve al ser humano como animal de compañía o para cazar.");
        descrip.put(
                "Gato",
                " Mamífero carnívoro doméstico, de la familia de los félidos, de patas cortas y uñas retráctiles, cabeza redonda, y pelo espeso y suave; suele tenerse como animal de compañía: el gato es un hábil cazador de ratones.");
        descrip.put(
                "Caballo",
                "Mamífero équido, macho, de cuerpo fuerte, orejas pequeñas, cola cubierta de largo pelo y patas terminadas en casco; es herbívoro, se domestica con facilidad y se suele usar para montar en él.");
        descrip.put(
                "Canario",
                "Pájaro de plumaje amarillo, verdoso o casi blanco, muy apreciado por su canto y que se suele criar como ave doméstica.");
        descrip.put(
                "Vaca",
                "Hembra del toro. De joven se denomina ternera, becerra, vaquilla o novilla. Su leche constituye un alimento completísimo y, después de la de burra, es la más parecida a la de la mujer.");
        descrip.put(
                "Cerdo",
                "Mamífero doméstico de cuerpo grueso, patas cortas, cabeza grande, orejas caídas, hocico chato y casi cilíndrico y cola en forma de hélice, que se cría para aprovechar su carne.");
    }*/

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
}
