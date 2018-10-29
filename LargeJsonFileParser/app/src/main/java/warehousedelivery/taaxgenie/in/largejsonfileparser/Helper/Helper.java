package warehousedelivery.taaxgenie.in.largejsonfileparser.Helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import warehousedelivery.taaxgenie.in.largejsonfileparser.MainActivity;
import warehousedelivery.taaxgenie.in.largejsonfileparser.R;

import static android.content.Context.ACTIVITY_SERVICE;
import static warehousedelivery.taaxgenie.in.largejsonfileparser.ReconGSTR2Activity.months_all;
import static warehousedelivery.taaxgenie.in.largejsonfileparser.ReconGSTR2Activity.statecodes;
import static warehousedelivery.taaxgenie.in.largejsonfileparser.ReconGSTR2Activity.statesList;
/*
  Created by Harshit on 11/11/2016.
 */

public class Helper {

    private static final String TAG = Helper.class.getSimpleName();
    private boolean flag = true;
    private static Context context;
    private static Activity activity;
    private String userChoosenTask, cNumber;
    private final int REQUEST_CAMERA = 1, SELECT_FILE = 2;
    private static Typeface regular_font, light_font, thin_font, roboto_font, greatVibes_font;

    public Helper(Context context) {
        Helper.context = context;
        activity = (Activity) context;
    }

    public void loadAssets() {

        regular_font = Typeface.createFromAsset(context.getAssets(), "fonts/mission_gothic_regular-webfont.ttf");
        light_font = Typeface.createFromAsset(context.getAssets(), "fonts/mission_gothic_light-webfont.ttf");
        thin_font = Typeface.createFromAsset(context.getAssets(), "fonts/mission_gothic_thin-webfont.ttf");
        roboto_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        greatVibes_font = Typeface.createFromAsset(context.getAssets(), "fonts/GreatVibes-Regular.otf");

    }

    /**********************
     * Validating Empty Imput
     **************************************/
    public static boolean isValidEmpty(String value) {

        boolean check;
        check = value.equalsIgnoreCase("") || value.length() < 1 || value.isEmpty();

        return check;
    }

    /**********************
     * Validating Email Address
     **************************************/
    public static boolean isValidEmail(CharSequence target) {

        boolean check;
        check = !Patterns.EMAIL_ADDRESS.matcher(target).matches();

        return check;
    }

    /**********************
     * Validating Mobile Number
     **************************************/
    public static boolean isValidMobileNu(String value) {

        boolean check;
        check = value.length() != 10;

        return check;
    }

    /**********************
     * Validating Password
     **************************************/

    public static boolean isValidPassword(String password) {
        boolean check;
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?#^&])[A-Za-z\\d$@$!%*?#^&]{8,}");
        Matcher matcher = pattern.matcher(password);

        if (matcher.matches())
            check = false;
        else
            check = true;
        /*if (password.length() < 8) {
            check = false;
            Toast.makeText(context, "Password is too weak", Toast.LENGTH_SHORT).show();
        } else if (password.length() > 13) {
            check = false;
            Toast.makeText(context, "Password should be less than 13 characters", Toast.LENGTH_SHORT).show();
        } else {
            check = true;
        }*/
        return check;
    }

    /**********************
     * Validating Website IPADDRESS
     **************************************/
    public boolean isValidURL(CharSequence target) {

        boolean check;
        check = !Patterns.WEB_URL.matcher(target).matches();

        return check;
    }

    /**********************
     * Validating Pincode
     **************************************/
    public boolean isValidPincode(String value) {

        boolean check;
        check = value.length() != 6;

        return check;
    }

    /**********************
     * Validating PAN Card
     **************************************/
    /*public boolean isValidPAN(String value) {

        boolean check;
        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
        Matcher matcher = pattern.matcher(value);

        if (matcher.matches())
            check = false;
        else
            check = true;

        return check;
    }*/

    /**********************
     * Validating GSTIN Number
     **************************************/
   /* public boolean isValidGSTIN(String value) {

        boolean check = true;                  //  12   ABCDE   1234     A       A/1     Z    1
        Pattern pattern = Pattern.compile("\\d{2}[A-Z]{5}\\d{4}[A-Z]{1}\\d{1}[Z][A-Z0-9]{1}");
        Matcher matcher = pattern.matcher(value);

        if (matcher.matches())
            for (int i = 0; i < statecodes.length; i++) {
                if (statecodes[i].equalsIgnoreCase(value.substring(0, 2))) {
                    check = false;
                    break;
                } else {
                    check = true;
                }
            }
        else
            check = true;

        return check;
    }*/

    /**********************
     * Loading Images
     **************************************/
    public static void loadImageProfile(ImageView imageView, String url, int image) {
        if (!url.equalsIgnoreCase("")) {
            //.load(url+ "?" + new Date().getTime())
            imageView.invalidate();
            Picasso.with(context)
                    .load(url)
                    .placeholder(R.mipmap.ic_launcher)   // optional
                    .error(R.mipmap.ic_launcher)      // optional
//                .resize(200, 200)                        // optional
//                .rotate(90)                             // optional
                    .into(imageView);
        } else {
            //.load(url+ "?" + new Date().getTime())
            imageView.invalidate();
            Picasso.with(context)
                    .load(image)
                    .placeholder(R.mipmap.ic_launcher)   // optional
                    .error(R.mipmap.ic_launcher)      // optional
//                .resize(200, 200)                        // optional
//                .rotate(90)                             // optional
                    .into(imageView);
        }
    }
/*
    public void convertDate(String date)R.mipmap.ic_launcher
    {
        try
        {
            String[] eventdate = new String[3];
            if (!date.equalsIgnoreCase(""))
            {
                eventdate = date.split("-");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

    public static Calendar defaultCalendar() {
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(2017,Calendar.JULY,01);
        return currentDate;
    }

    public static String convertDateFormat(String date,String oldFormat, String newFormat){
        String newDate = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
        Date date11;
        try {
            date11 = sdf1.parse(date);
            sdf1 = new SimpleDateFormat(newFormat);
            newDate = sdf1.format(date11);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    /******************************Date Picker max Date*****************************/
    public static void dateSetMaxData(final Activity activity, final EditText setDateTinText) {
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; // your format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                setDateTinText.setText(sdf.format(myCalendar.getTime()));
            }

        };
        DatePickerDialog dialog = new DatePickerDialog(activity, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();

        //new DatePickerDialog(activity, date, myCalendar.get(myCalendar.YEAR), myCalendar.get(myCalendar.MONTH), myCalendar.get(myCalendar.DAY_OF_MONTH)).show();

    }

    /******************************Date Picker Min Date*****************************/
    public static void dateSetMinData(final Activity activity, final EditText setDateTinText) {
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; // your format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                setDateTinText.setText(sdf.format(myCalendar.getTime()));
            }

        };
        DatePickerDialog dialog = new DatePickerDialog(activity, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.show();

        //new DatePickerDialog(activity, date, myCalendar.get(myCalendar.YEAR), myCalendar.get(myCalendar.MONTH), myCalendar.get(myCalendar.DAY_OF_MONTH)).show();

    }

    /******************************Date Picker Min Date*****************************/
    public static void dateSetStartEnd(final Activity activity, final TextView setDateTinText) {

        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; // your format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                setDateTinText.setText(sdf.format(myCalendar.getTime()));
            }

        };
        DatePickerDialog dialog = new DatePickerDialog(activity, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(defaultCalendar().getTimeInMillis());
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        System.out.println("InvoiceDate: " + defaultCalendar().getTimeInMillis());
        System.out.println("CurrentDate: " + System.currentTimeMillis());
        dialog.show();

        //new DatePickerDialog(activity, date, myCalendar.get(myCalendar.YEAR), myCalendar.get(myCalendar.MONTH), myCalendar.get(myCalendar.DAY_OF_MONTH)).show();

    }

    /******************************Date Picker Min Date*****************************/
    public static void dateSetCustomStartEnd(final Activity activity, final TextView setDateTinText, String invDate) {

        final Calendar myCalendar = Calendar.getInstance();
        String[] splitDate = new String[3];
        if (!invDate.equalsIgnoreCase("")) {
            splitDate = invDate.split("-");
        }
        Calendar invDateCompare = Calendar.getInstance();

        int day = Integer.parseInt(splitDate[0]);
        int month = Integer.parseInt(splitDate[1]) - 1;
        int year = Integer.parseInt(splitDate[2]);
        invDateCompare.set(year, month, day);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; // your format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                setDateTinText.setText(sdf.format(myCalendar.getTime()));
            }

        };
        DatePickerDialog dialog = new DatePickerDialog(activity, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(invDateCompare.getTimeInMillis());
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        System.out.println("InvoiceDate: " + invDateCompare.getTimeInMillis());
        System.out.println("CurrentDate: " + System.currentTimeMillis());
        dialog.show();

        //new DatePickerDialog(activity, date, myCalendar.get(myCalendar.YEAR), myCalendar.get(myCalendar.MONTH), myCalendar.get(myCalendar.DAY_OF_MONTH)).show();

    }

    /******************************Notification date and time code*****************************/
    public String convertNotificationDate(String notidate, String notitime) {
        String datereturn;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
        Date date = null;

        try {
            date = formatter.parse(notidate);
            System.out.println("date:" + date);

            Date date1 = new Date();

            if (date.getDay() != date1.getDay()) {
                String fulldatereturn = date.toString();
                String[] eventdate = new String[6];
                if (!fulldatereturn.equalsIgnoreCase("")) {
                    eventdate = fulldatereturn.split(" ");
                }

                datereturn = eventdate[2] + " " + eventdate[1] + " " + "at " + notitime;
                System.out.println("datereturn:" + datereturn);
                return datereturn;
            } else {
                //time convert code
                long mills = date.getTime() - date1.getTime();
                int Hours = Math.abs((int) (mills / (1000 * 60 * 60)));
                int Mins = Math.abs((int) (mills / (1000 * 60)));

                String diff = Hours + ":" + Mins;

                if (Hours < 1) {
                    datereturn = Mins + " min ago";
                } else {
                    datereturn = Hours + " hours ago";
                }
                return datereturn;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /******************************END     Notification date and time code     END *****************************/

    public String[] convertTime(String time) {
        String[] daytime = new String[2];
        String[] fulltime = new String[2];
        try {
            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm", Locale.US);
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
            Date date = null;
            date = parseFormat.parse(time);

            String fulldate = displayFormat.format(date);
            System.out.println(parseFormat.format(date) + " = " + displayFormat.format(date));

            if (!fulldate.equalsIgnoreCase("")) {
                fulltime = fulldate.split(":");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fulltime;
    }

    public static void splitDate(TextView setDate, TextView setMonth, String invDate) {
        String[] splitDate = new String[3];
        if (!invDate.equalsIgnoreCase("")) {
            splitDate = invDate.split("-");
        }
        setDate.setText(splitDate[0]);
        int mon = Integer.parseInt(splitDate[1]);
        setMonth.setText(months_all[mon - 1] + " " + splitDate[2]);
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }


    public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {

        /*Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        Bitmap resizedcropped = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
        Bitmap bmCropped = valid.getRoundedRectBitmap(resizedcropped, 100);*/

        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            //int color = 0xff424242;
            int color = 0xffffffff;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, 200, 200);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(pixels, pixels, pixels, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError o) {
            o.printStackTrace();
        }
        return result;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    //Hide keyboard
    public static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }


    /*public void successPopUp(String message, final Context context) {
        final boolean[] flag = {false};
        final Activity activity = (Activity) context;
        final Dialog prodialog = new Dialog(context);
        prodialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        prodialog.setContentView(R.layout.success_popup);
        prodialog.setCancelable(false);

        TextView msg = (TextView) prodialog.findViewById(R.id.statusmessage);

        Button done = (Button)prodialog.findViewById(R.id.done);
        msg.setText(message);

        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                prodialog.dismiss();
                activity.finish();
            }
        });

        prodialog.show();

    }*/


    /******************************Date Picker max Date*****************************/
    public static void dateMaxConvert(final Activity activity, final TextView setDateTinText) {
        try {
            final Calendar myCalendar = Calendar.getInstance();
            Calendar invDateCompare = Calendar.getInstance();
            invDateCompare.set(Calendar.MONTH,6);
            invDateCompare.set(Calendar.YEAR,2017);

        /*DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub

                *//*myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; // your format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                String selectedDate = sdf.format(myCalendar.getTime());
                String [] splitDate = new String[3];
                if (!selectedDate.equalsIgnoreCase("")) {
                    splitDate = selectedDate.split("-");
                }*//*
                String month = months_all[monthOfYear];
                setDateTinText.setText(month +" " +year);
            }

        };

        DatePickerDialog dialog = new DatePickerDialog(activity, date, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();*/
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String month = months_all[monthOfYear];
                                setDateTinText.setText(month + " " + year);
                                Log("test", String.valueOf(monthOfYear));
                                Log("test", (dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));

                            }
                        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(invDateCompare.getTimeInMillis());
                datePickerDialog.getDatePicker().setMinDate(invDateCompare.getTimeInMillis());
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
//        ((ViewGroup) datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
                datePickerDialog.show();
                                        } else {*/
            DatePickerDialog datePickerDialog = new DatePickerDialog(activity, AlertDialog.THEME_HOLO_DARK,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            String month = months_all[monthOfYear];
                            setDateTinText.setText(month + " " + year);
                            Log("test", String.valueOf(monthOfYear));
                            Log("test", (dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));

                        }
                    }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(invDateCompare.getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.getDatePicker().findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
            datePickerDialog.show();
            //        }
        }catch (Exception e){
            System.out.println("Date set error"+e);
        }
    }

    public static void dateMaxConvert(final Activity activity, final TextView setDateTinText, String invDate) {
        try {

            final Calendar myCalendar = Calendar.getInstance();
            String[] splitDate = new String[2];
            if (!invDate.equalsIgnoreCase("")) {
                splitDate = invDate.split(" ");
            }
            Calendar invDateCompare = Calendar.getInstance();

            int month=0;
            for(int i=0;i<months_all.length;i++)
            {
                if(months_all[i].equalsIgnoreCase(splitDate[0]))
                {
                    month = i;
                    break;
                }
            }

            int year = Integer.parseInt(splitDate[1]);
            invDateCompare.set(year, month,1);

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                String month = months_all[monthOfYear];
                                setDateTinText.setText(month + " " + year);
                                Log("test", String.valueOf(monthOfYear));
                                Log("test", (dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));

                            }
                        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(invDateCompare.getTimeInMillis());
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
//        ((ViewGroup) datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
                datePickerDialog.show();
            } else {*/
            DatePickerDialog datePickerDialog = new DatePickerDialog(activity, AlertDialog.THEME_HOLO_DARK,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            String month = months_all[monthOfYear];
                            setDateTinText.setText(month + " " + year);
                            Log("test", String.valueOf(monthOfYear));
                            Log("test", (dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));

                        }
                    }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(invDateCompare.getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            System.out.println("InvoiceDate: " + invDateCompare.getTimeInMillis());
            System.out.println("CurrentDate: " + System.currentTimeMillis());
            datePickerDialog.getDatePicker().findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
            datePickerDialog.show();
            //}
        }catch (Exception e){
            System.out.println("Date set error"+e);
        }
    }

    public static int getMonths(String date1, String date2)
    {
        int count=0;
        DateFormat formater = new SimpleDateFormat("MMM-yyyy");

        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();

        try {
            beginCalendar.setTime(formater.parse(date1));
            finishCalendar.setTime(formater.parse(date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        while (beginCalendar.before(finishCalendar)) {
            // add one month to date per loop
            String date =     formater.format(beginCalendar.getTime()).toUpperCase();
            System.out.println(date);
            count++;
            beginCalendar.add(Calendar.MONTH, 1);
        }
        return count;
    }

    public String printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        if (elapsedDays > 0)
            return elapsedDays + " days, " + elapsedHours + " hours, " + elapsedMinutes + " minutes, " + elapsedSeconds + " seconds";
        else if (elapsedHours > 0)
            return elapsedHours + " hours";
        else if (elapsedMinutes > 0)
            return elapsedMinutes + " minutes";
        else if (elapsedSeconds > 0)
            return elapsedSeconds + " seconds";
        else
            return "0";

    }


    public static String encodeURL(String url) {
        String encodedString = "";
        try {
            encodedString = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //Helper.Log(TAG, encodedString);

        return encodedString;
    }

    public static String formatAmount(double amount) {
        String formattedAmount="";

        BigDecimal d = new BigDecimal(amount);
        formattedAmount =  NumberFormat.getNumberInstance(new Locale("en", "IN")).format(d);

        return formattedAmount;
    }

    public static String formatAmountDouble(Double amount) {
        String formattedAmount = String.format("%.0f", amount);
        amount = Double.parseDouble(formattedAmount);

        BigDecimal d = new BigDecimal(amount);
        formattedAmount =  NumberFormat.getNumberInstance(new Locale("en", "IN")).format(d);

        return formattedAmount;
    }

    public static String format2DecAmountDouble(Double amount) {
        String formattedAmount = String.format("%.2f", amount);
        amount = Double.parseDouble(formattedAmount);

        BigDecimal d = new BigDecimal(amount);
        formattedAmount =  NumberFormat.getNumberInstance(new Locale("en", "IN")).format(d);

        return formattedAmount;
    }

    public static String formatAmountBigDecimal(String amount) {

        //amount = Double.parseDouble(BigDecimal.valueOf(amount).toPlainString());
        //String formattedAmount = NumberFormat.getNumberInstance(new Locale("en", "IN")).format(amount);
        BigDecimal d = new BigDecimal(amount);
        String formattedAmount =  NumberFormat.getNumberInstance(new Locale("en", "IN")).format(d);

        return formattedAmount;
    }

    public static void sendCrashReportDetails(final Activity activity, String errorMessage, StackTraceElement[] stackTrace) {

        /************ Handle crash and show exit and send report ***********/
        final JSONObject crashObject = new JSONObject();
        try {
            crashObject.put("companyId", "C");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        String errorReport = "null";
        String stackTraceReport = "";///*********** Stack Trace ************/
        String deviceDetail = "null";
        String telephonyDeviceId = "null";
        String imeiSIM1 = "null";
        PackageInfo pInfo = null;
        String versionCode = "null", versionName = "null";

        String androidSDK="null",androidVersion="null",androidBrand="null",
                androidManufacturer="null",androidModel="null",androidDeviceId="null";


        if(activity==null)
        {
            try {
                androidSDK = String.valueOf(android.os.Build.VERSION.SDK_INT);
                androidVersion = android.os.Build.VERSION.RELEASE;
                androidBrand = android.os.Build.BRAND;
                androidManufacturer = android.os.Build.MANUFACTURER;
                androidModel = android.os.Build.MODEL;
                deviceDetail = "App Version Code: " + versionCode + "\nApp Version Number: " + versionName + "\nSDK: " + androidSDK + "\nVersion: " + androidVersion + "\nBrand: " + androidBrand +
                        "\nManufacturer: " + androidManufacturer + "\nModel: " + androidModel;

                for (int i = 0; i < stackTrace.length; i++) {
                    if (i == 6)
                        break;
                    //int StackNo = i+1;
                    //stackTraceReport = stackTraceReport +"\n\\**********Stack Trace "+StackNo+" *********/\n";
                    stackTraceReport = String.format("%s\n%s", stackTraceReport, stackTrace[i].toString());/*"Class Name: "+stackTrace[i].getClassName()+"\nFile Name: "+stackTrace[i].getFileName()+
                        "\nMethod Name: "+stackTrace[i].getMethodName()+"\nLine Number: "+stackTrace[i].getLineNumber();*/
                }


                errorReport = deviceDetail + "\nError Message: " + errorMessage + "\nError Stack Trace: \n" + stackTraceReport;
            }catch (Exception e) {
                e.printStackTrace();
                Log(TAG, String.valueOf(e.getMessage()));
                errorReport = errorReport + "Error Message: " + e.getMessage() + "\n";
            }


        }else {


            // App Version Details
            try {
                pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
                versionCode = String.valueOf(pInfo.versionCode).trim();
                versionName = String.valueOf(pInfo.versionName).trim();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                Log(TAG, String.valueOf(e.getMessage()));
                errorReport = "Error Message: " + e.getMessage() + "\n";
            }


            //Device ID details
        /*TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); //ANDROID DEVCIE ID
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            if (TelephonyMgr != null) {
                telephonyDeviceId = TelephonyMgr.getDeviceId();
                imeiSIM1 = TelephonyMgr.getSimSerialNumber();
            }

        }*/

            try {
                androidSDK = String.valueOf(android.os.Build.VERSION.SDK_INT);
                androidVersion = android.os.Build.VERSION.RELEASE;
                androidBrand = android.os.Build.BRAND;
                androidManufacturer = android.os.Build.MANUFACTURER;
                androidModel = android.os.Build.MODEL;
                androidDeviceId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
                deviceDetail = "App Version Code: " + versionCode + "\nApp Version Number: " + versionName + "\nSDK: " + androidSDK + "\nVersion: " + androidVersion + "\nBrand: " + androidBrand +
                        "\nManufacturer: " + androidManufacturer + "\nModel: " + androidModel + "\nAndroid Device Id: " + androidDeviceId;

                for (int i = 0; i < stackTrace.length; i++) {
                    if (i == 6)
                        break;
                    //int StackNo = i+1;
                    //stackTraceReport = stackTraceReport +"\n\\**********Stack Trace "+StackNo+" *********/\n";
                    stackTraceReport = String.format("%s\n%s", stackTraceReport, stackTrace[i].toString());/*"Class Name: "+stackTrace[i].getClassName()+"\nFile Name: "+stackTrace[i].getFileName()+
                        "\nMethod Name: "+stackTrace[i].getMethodName()+"\nLine Number: "+stackTrace[i].getLineNumber();*/
                }

                errorReport = deviceDetail + "\nTelephony Device ID " + telephonyDeviceId + "\nIMEI No: " + imeiSIM1 + "\nError Message: " + errorMessage + "\nError Stack Trace: \n" + stackTraceReport;
            } catch (Exception e) {
                e.printStackTrace();
                Log(TAG, String.valueOf(e.getMessage()));
                errorReport = errorReport + "Error Message: " + e.getMessage() + "\n";
            }
        }

        try {
            crashObject.put("AppVersionCode",versionCode);
            crashObject.put("AppVersionNumber",versionName);
            crashObject.put("SDK",androidSDK);
            crashObject.put("Version",androidVersion);
            crashObject.put("Brand",androidBrand);
            crashObject.put("Manufacturer",androidManufacturer);
            crashObject.put("Model",androidModel);
            crashObject.put("AndroidDeviceId",androidDeviceId);
            crashObject.put("TelephonyDeviceID",telephonyDeviceId);
            crashObject.put("IMEINo",imeiSIM1);
            crashObject.put("ErrorMessage",errorMessage);
            crashObject.put("ErrorStackTrace",stackTraceReport);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(activity!=null) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setTitle("Error Tax Efiling crashed");
            alertDialogBuilder.setMessage("We are very sorry that this crash occured. Please help us track down and fix this crash by providing detailed information about this.\nThanks for your help in improving the app");

            final String finalErrorReport = errorReport;
            alertDialogBuilder.setPositiveButton("Send Report",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Log(TAG, finalErrorReport);
                            Toast.makeText(activity, "Report Sent Succesfully", Toast.LENGTH_SHORT).show();
                            //api Call
                            Intent intent = new Intent(activity, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("EXIT", true);
                            activity.startActivity(intent);

                        }
                    });

            alertDialogBuilder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    activity.startActivity(intent);
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }else
        {
        }


        /************ Handle crash and show only exit ***********/
        /*final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Error");
        alertDialogBuilder.setMessage("Sorry for the inconvenience. Please send crash report so that we can fix it.");

        final String finalErrorReport = errorReport;
        alertDialogBuilder.setPositiveButton("Exit",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(context, HomeScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        context.startActivity(intent);
                        //api Call
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();*/

    }


    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    public  static String nullcheckString(String data) {
        String checknull="";
        if(data==null)
            return "";
        else if (data.equalsIgnoreCase("null")) {

            return "";
        }
        else{
            return  data;
        }

    }
    public  static  String nullcheckDouble(String data) {
        String checknull="0.0";
        if (data.equalsIgnoreCase("null")||data.equalsIgnoreCase("")) {

            return "0.0";
        }else{
            return  data;
        }

    }

    //For State
    public static String getStateCode(String stateName){

        String code="0";
        for(int i=0;i<statesList.length;i++)
        {
            if(statesList[i].equalsIgnoreCase(stateName)) {
                code = statecodes[i];
                break;
            }
        }

        return code;
    }
    public static String getStateName(String stateCode){

        String name="";
        for(int i=0;i<statecodes.length;i++)
        {
            if(statecodes[i].equalsIgnoreCase(stateCode)) {
                name = statesList[i];
                break;
            }
        }
        return name;
    }

    // method for bitmap to base64
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    // Get a MemoryInfo object for the device's current memory status.
    public static ActivityManager.MemoryInfo getAvailableMemory() {
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }


    // Set margin in Settings
    public static void setMargin(RecyclerView mRecyclerView,int height)
    {
        ViewGroup.MarginLayoutParams marginLayoutParams =
                (ViewGroup.MarginLayoutParams) mRecyclerView.getLayoutParams();
        marginLayoutParams.setMargins(0, 0, 0, height);
        mRecyclerView.setLayoutParams(marginLayoutParams);
    }

    public static String getMonth(int month)
    {
        String returnMonth = String.valueOf(month);
        if(returnMonth.length()==1){
            returnMonth = "0"+returnMonth;
        }
        Helper.Log(TAG, returnMonth);

        return returnMonth;
    }

    public static void Log(String tag, String msg) {
        Log.e(tag, msg);
    }
}

