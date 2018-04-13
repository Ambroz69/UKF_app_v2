package com.example.dominik.ukf_app.db_connect;


public class Api {

    private static final String ROOT_URL = "https://dpecuch17.student.ki.fpv.ukf.sk/UKF_app/v1/Api.php?apicall=";

    public static final String URL_READ_PODMIENKY_PRIJATIA = ROOT_URL + "getpodmienkyprijatiainfo";
    public static final String URL_READ_STUDENTSKY_ZIVOT = ROOT_URL + "getstudentskyzivotinfo";
    public static final String URL_READ_UDALOSTI = ROOT_URL + "getudalostiinfo";
    public static final String URL_READ_STUDIJNY_PROGRAM = ROOT_URL + "getstudijnyprograminfo";
    public static final String URL_READ_IMAGES = ROOT_URL + "getimages";
}

