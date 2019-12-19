package com.example.mydschoolteachersapp.Classes;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Singelton {

        private static Singelton mySingleTon;
        private RequestQueue requestQueue;
        private static Context mctx;
        private Singelton(Context context){
            mctx=context;
            this.requestQueue=getRequestQueue();

        }
        public RequestQueue getRequestQueue(){
            if (requestQueue==null){
                requestQueue= Volley.newRequestQueue(mctx.getApplicationContext());
            }
            return requestQueue;
        }
        public static synchronized Singelton getInstance(Context context){
            if (mySingleTon==null){
                mySingleTon=new Singelton(context);
            }
            return mySingleTon;
        }
        public<T> void addToRequestQue(Request<T> request){
            requestQueue.add(request);

        }
    }


