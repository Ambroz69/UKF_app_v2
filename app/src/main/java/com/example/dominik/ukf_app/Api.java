package com.example.dominik.ukf_app;


public class Api {

    private static final String ROOT_URL = "https://dpecuch17.student.ki.fpv.ukf.sk/UKF_app/v1/Api.php?apicall=";

    public static final String URL_CREATE_ITEM = ROOT_URL + "createitem";
    public static final String URL_READ_ITEMS = ROOT_URL + "getitems";
    public static final String URL_UPDATE_ITEM = ROOT_URL + "updateitem";
    public static final String URL_DELETE_ITEM = ROOT_URL + "deleteitem&id=";
    public static final String URL_READ_PODMIENKY_PRIJATIA = ROOT_URL + "getpodmienkyprijatiainfo";
    public static final String URL_READ_STUDENTSKY_ZIVOT = ROOT_URL + "getstudentskyzivotinfo";


}

