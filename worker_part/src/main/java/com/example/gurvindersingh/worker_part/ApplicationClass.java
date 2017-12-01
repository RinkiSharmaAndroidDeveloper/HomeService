package com.example.gurvindersingh.worker_part;

import android.app.Application;

import com.example.gurvindersingh.worker_part.Pojo_Clases.Services_Data;

import java.util.ArrayList;

/**
 * Created by Gurvinder Singh on 10/4/2016.
 */
public class ApplicationClass extends Application {
   public static ArrayList<String> Products_Name_of_sub=new ArrayList<String>();
   public static ArrayList<Integer> ColorCode=new ArrayList<Integer>();
   public static ArrayList<Services_Data> ServiceDataList=new ArrayList<Services_Data>();
}
