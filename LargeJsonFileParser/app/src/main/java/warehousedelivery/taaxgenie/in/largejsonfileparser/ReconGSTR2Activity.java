package warehousedelivery.taaxgenie.in.largejsonfileparser;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONArray;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

import warehousedelivery.taaxgenie.in.largejsonfileparser.Helper.AppController;
import warehousedelivery.taaxgenie.in.largejsonfileparser.Helper.ConnectionDetector;
import warehousedelivery.taaxgenie.in.largejsonfileparser.Helper.Helper;
import warehousedelivery.taaxgenie.in.largejsonfileparser.Helper.Stopwatch;

public class ReconGSTR2Activity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private final String TAG = ReconGSTR2Activity.class.getSimpleName();
    private RecyclerView rvAllInvoice;
    private Button btnRetry;

    private RelativeLayout rr_NoConnection;
    private TextView list;


    private String type = "",typename="",gstin="";
    private TextView summary_supp_gstin,summary_supp_legal_name,summary_supp_trade_name,summary_recon_period,summary_excess;
    private RelativeLayout rl_single, rl_double;
    private TextView total_invoice_text, total_invoice_value, total_taxable_text, total_taxable_value;
    private TextView gstr2a_total_invoice_value, purchase_total_invoice_value, gstr2a_total_taxable_value, purchase_total_taxable_value;
    private TextView nodate,matchUndo,mismatchkeepgstr2a,invMismatchAccept,invMismatchReject, inGstr2aAccept,inGstr2aManualMatchingPR,inPrAccept,inPrUndo,inPrmanualMatching2A;

    public static ArrayList<MismatchReports> mismatchReportList = new ArrayList<>();
    public static String[] months_all = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
    public static String[] months_full = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    public final static String[] statesList = {"Andaman & Nicobar Islands","Andhra Pradesh","Arunachal Pradesh","Assam","Bihar","Chandigarh","Chhattisgarh","Dadra & Nagar Haveli","Daman & Diu","Delhi","Goa","Gujarat","Haryana","Himachal Pradesh","Jammu & Kashmir","Jharkhand","Karnataka","Kerala","Lakshdweep","Madhya Pradesh","Maharashtra","Manipur","Meghalaya","Mizoram","Nagaland","Odisha","Other Territory","Pondicherry","Punjab","Rajasthan","Sikkim","Tamil Nadu","Telengana","Tripura","Uttar Pradesh","Uttarakhand","West Bengal"};
    public final static String[] unionStatesList = {"Andaman & Nicobar Islands","Chandigarh","Dadra & Nagar Haveli","Daman & Diu","Delhi","Lakshdweep","Pondicherry"};
    public final static String[] statecodes = {"35","37","12","18","10","04","22","26","25","07","30","24","06","02","01","20","29","32","31","23","27","14","17","15","13","21","98","34","03","08","11","33","36","16","09","05","19"};

    //RECON INVOICE TYPE SUMMARY
    @BindView(R.id.ll_all_summary)                  LinearLayout ll_all_summary;

    //MATCHED
    @BindView(R.id.matched_no_of_invoices)          TextView matched_no_of_invoices;
    @BindView(R.id.matched_invoice_value)           TextView matched_invoice_value;
    @BindView(R.id.matched_taxable_value)           TextView matched_taxable_value;
    @BindView(R.id.matched_tax_value)               TextView matched_tax_value;

    //MISMATCHED
    @BindView(R.id.mismatched_no_of_invoices)       TextView mismatched_no_of_invoices;
    @BindView(R.id.mismatched_invoice_value)        TextView mismatched_invoice_value;
    @BindView(R.id.mismatched_taxable_value)        TextView mismatched_taxable_value;
    @BindView(R.id.mismatched_tax_value)            TextView mismatched_tax_value;

    //INVOICE MISMATCHED
    @BindView(R.id.invMismatched_no_of_invoices)    TextView invMismatched_no_of_invoices;
    @BindView(R.id.invMismatched_invoice_value)     TextView invMismatched_invoice_value;
    @BindView(R.id.invMismatched_taxable_value)     TextView invMismatched_taxable_value;
    @BindView(R.id.invMismatched_tax_value)         TextView invMismatched_tax_value;

    //GSTR2A ONLY
    @BindView(R.id.gstr2a_no_of_invoices)           TextView gstr2a_no_of_invoices;
    @BindView(R.id.gstr2a_invoice_value)            TextView gstr2a_invoice_value;
    @BindView(R.id.gstr2a_taxable_value)            TextView gstr2a_taxable_value;
    @BindView(R.id.gstr2a_tax_value)                TextView gstr2a_tax_value;

    //PR ONLY
    @BindView(R.id.pr_no_of_invoices)               TextView pr_no_of_invoices;
    @BindView(R.id.pr_invoice_value)                TextView pr_invoice_value;
    @BindView(R.id.pr_taxable_value)                TextView pr_taxable_value;
    @BindView(R.id.pr_tax_value)                    TextView pr_tax_value;

    //TOTAL
    @BindView(R.id.recon_total_no_of_invoices)      TextView recon_total_no_of_invoices;
    @BindView(R.id.recon_total_invoice_value)       TextView recon_total_invoice_value;
    @BindView(R.id.recon_total_taxable_value)       TextView recon_total_taxable_value;
    @BindView(R.id.recon_total_tax_value)           TextView recon_total_tax_value;
    @BindView(R.id.progress)                        ProgressBar progress;


    private MismatchAdapter mismatchAdapter;
    private ArrayList<MismatchReports> tempMisMatchList = new ArrayList<>();
    private ArrayList<MismatchReports> AllMisMatchList = new ArrayList<>();
    //private ImageView navbutton;
    private RelativeLayout match,mismatch,invMismatch,inGstr2a,inPR;
    private boolean flag = false;


    private String returnMonth = "";
    private int oldmonth, oldyear;
    private String ButtonStatus = "";
    private ArrayList<MismatchReports> Checked_List = new ArrayList<>();
    private LinearLayout checkboxlinear, data, buttonslayout;
    private DisplayMetrics dm;
    private AlertDialog getGstr2ASessionProgressDialog, getGstr2AProgressDialog;
    private ArrayList<MismatchReports> tempMisMatchListforsearch = new ArrayList<>();
    //SearchView recon_search;
    //Spinner recon_spinner;
    private String selectedType = "";
    String respon="",fullDate="",legalName="",deficit="";
    String dateFromTo[];
    private Gson gson;

    //String IPADDRESS ="https://gst.taxgenie.online/gstsvrc-0.0.1/";
    //String IPADDRESS ="https://demo.taxgenie.online/gstsvrc-0.0.1/";
    String IPADDRESS ="http://35.154.23.209:8080/gstsvrc-0.0.1/";

  ConnectionDetector homeConnDetector;

    public static int countReason = 0;
    private SearchView searchView;
    private MenuItem filter;
    public static MenuItem checkAll;
    private MenuItem exportExcel;

    final int MSG_START_TIMER = 0;
    final int MSG_STOP_TIMER = 1;
    final int MSG_UPDATE_TIMER = 2;

    Stopwatch timer = new Stopwatch();
    final int REFRESH_RATE = 100;

    Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_START_TIMER:
                    timer.start(); //start timer
                    mHandler.sendEmptyMessage(MSG_UPDATE_TIMER);
                    break;

                case MSG_UPDATE_TIMER:
                    //Helper.Log("Update",""+ timer.getElapsedTime());
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIMER,REFRESH_RATE); //text view is updated every second,
                    break;                                  //though the timer is still running
                case MSG_STOP_TIMER:
                    mHandler.removeMessages(MSG_UPDATE_TIMER); // no more updates.
                    timer.stop();//stop timer

                    int Seconds = (int) (timer.getElapsedTime() / 1000);
                    int Minutes = Seconds / 60;
                    Seconds = Seconds % 60;
                    int MilliSeconds = (int) (timer.getElapsedTime() % 1000);

                    Helper.Log("Stop",""+ "" + Minutes + ":"
                            + String.format("%02d", Seconds) + ":"
                            + String.format("%03d", MilliSeconds));
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recon_gstr2);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(ReconGSTR2Activity.this, R.color.returns_green));
        }
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        homeConnDetector =ConnectionDetector.getInstance(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.returns_green)));
        getSupportActionBar().setTitle("All Records");

        ButterKnife.bind(this);

        GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

//        Bundle b = getIntent().getExtras();
//        if (b != null) {
//            type = b.getString("Type");
//            typename=b.getString("TypeName");
//            respon=b.getString("response","{\"summary\":[]}");
//            Helper.Log(TAG,respon);
//            gstin = b.getString("gstin","");
//            Helper.Log(TAG,gstin);
//            fullDate = b.getString("date");
//            Helper.Log(TAG,fullDate);
//            dateFromTo = b.getString("date").split(",");
//            if(type.equalsIgnoreCase("ALL_Records"))
//            {
//                legalName = b.getString("legalName");
//                deficit = b.getString("deficit");
//            }
//        }

        summary_supp_gstin = findViewById(R.id.summary_supp_gstin);
        summary_supp_legal_name = findViewById(R.id.summary_supp_legal_name);
        summary_supp_trade_name = findViewById(R.id.summary_supp_trade_name);
        summary_recon_period = findViewById(R.id.summary_recon_period);
        summary_excess = findViewById(R.id.summary_excess);

        rl_single = findViewById(R.id.rl_single);
        rl_double = findViewById(R.id.rl_double);
        total_invoice_text = findViewById(R.id.total_invoice_text);
        total_invoice_value = findViewById(R.id.total_invoice_value);
        total_taxable_text = findViewById(R.id.total_taxable_text);
        total_taxable_value = findViewById(R.id.total_taxable_value);
        gstr2a_total_invoice_value = findViewById(R.id.gstr2a_total_invoice_value);
        purchase_total_invoice_value = findViewById(R.id.purchase_total_invoice_value);
        gstr2a_total_taxable_value = findViewById(R.id.gstr2a_total_taxable_value);
        purchase_total_taxable_value = findViewById(R.id.purchase_total_taxable_value);

        matchUndo = findViewById(R.id.matchUndo);
        mismatchkeepgstr2a = findViewById(R.id.mismatchkeepgstr2a);
        invMismatchAccept = findViewById(R.id.invMismatchAccept);
        invMismatchReject = findViewById(R.id.invMismatchReject);
        inGstr2aAccept = findViewById(R.id.inGstr2aAccept);
        inGstr2aManualMatchingPR = findViewById(R.id.inGstr2aManualMatchingPR);
        inPrAccept = findViewById(R.id.inPrAccept);
        inPrUndo = findViewById(R.id.inPrUndo);
        inPrmanualMatching2A = findViewById(R.id.inPrmanualMatching2A);

        data = findViewById(R.id.data);
        rr_NoConnection = findViewById(R.id.rr_NoConnection);

        match = findViewById(R.id.match);
        invMismatch = findViewById(R.id.invMismatch);
        mismatch = findViewById(R.id.mismatch);
        buttonslayout = findViewById(R.id.buttonslayout);
        inGstr2a = findViewById(R.id.inGstr2a);
        inPR = findViewById(R.id.inPR);

        list = findViewById(R.id.list);
        btnRetry = findViewById(R.id.btnRetry);
        rr_NoConnection.setVisibility(View.GONE);

//        summary_supp_gstin.setText(gstin);
//        summary_supp_legal_name.setText(legalName);
//        summary_supp_trade_name.setText("");
//        summary_recon_period.setText(months_all[Integer.parseInt(dateFromTo[0])-1] + "-" + dateFromTo[1] + " to " + months_all[Integer.parseInt(dateFromTo[2])-1] + "-" + dateFromTo[3]);
//        summary_excess.setText(deficit);

        rvAllInvoice = findViewById(R.id.rvAllInvoice);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvAllInvoice.setLayoutManager(mLayoutManager);
        rvAllInvoice.setNestedScrollingEnabled(false);
        rvAllInvoice.setHasFixedSize(false);
        RecyclerView.RecycledViewPool mSharedPool = new RecyclerView.RecycledViewPool();
        rvAllInvoice.setRecycledViewPool(mSharedPool);

        nodate = findViewById(R.id.nodate);

        ViewGroup.MarginLayoutParams marginLayoutParams =
                (ViewGroup.MarginLayoutParams) mismatch.getLayoutParams();
        if (dm.heightPixels < 1900) {
            marginLayoutParams.height = 80;
        } else {
            marginLayoutParams.height = 150;
        }

        match.setLayoutParams(marginLayoutParams);
        mismatch.setLayoutParams(marginLayoutParams);
        inGstr2a.setLayoutParams(marginLayoutParams);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReconciliationReport("27AAACC1645G1ZZ", "07", "2017","08","2017");
            }
        });

        matchUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Checked_List.clear();
                ButtonStatus = "Undo";
                for (int i = 0; i < mismatchReportList.size(); i++) {
                    if (mismatchReportList.get(i).isSelectcheckbox()) {
                        Checked_List.add(mismatchReportList.get(i));
                    }
                }
                if (Checked_List.size() == 0) {
                    Toast.makeText(ReconGSTR2Activity.this, "Please Select Invoice", Toast.LENGTH_SHORT).show();
                } else {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReconGSTR2Activity.this);
                    alertDialogBuilder.setMessage(Checked_List.size() + " Invoice selected. Are you sure you want Undo?");// Invoices for " + dateFromTo[0] + " " + dateFromTo[1] + " - " + dateFromTo[2] + " " + dateFromTo[3] + "?");
                    alertDialogBuilder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int arg1) {
                                    dialog.dismiss();


                                }
                            });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });


        mismatchkeepgstr2a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checked_List.clear();
                ButtonStatus = "KeepGstr2A";
                for (int i = 0; i < mismatchReportList.size(); i++) {
                    if (mismatchReportList.get(i).isSelectcheckbox()) {
                        Checked_List.add(mismatchReportList.get(i));
                    }
                }
                if (Checked_List.size() == 0) {
                    Toast.makeText(ReconGSTR2Activity.this, "Please Select  Invoice", Toast.LENGTH_SHORT).show();
                } else {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReconGSTR2Activity.this);
                    alertDialogBuilder.setMessage(Checked_List.size() + " Invoice selected. Are you sure you want KeepGstr2A?");// Invoices for " + dateFromTo[0] + " " + dateFromTo[1] + " - " + dateFromTo[2] + " " + dateFromTo[3] + "?");
                    alertDialogBuilder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int arg1) {
                                    dialog.dismiss();

                                    //AcceptReconciliationReport();
                                }
                            });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });

        invMismatchAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checked_List.clear();
                ButtonStatus = "Accept";
                for (int i = 0; i < mismatchReportList.size(); i++) {
                    if (mismatchReportList.get(i).isSelectcheckbox()) {
                        Checked_List.add(mismatchReportList.get(i));
                    }
                }
                if (Checked_List.size() == 0) {
                    Toast.makeText(ReconGSTR2Activity.this, "Please Select Invoice", Toast.LENGTH_SHORT).show();
                } else {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReconGSTR2Activity.this);
                    alertDialogBuilder.setMessage("Are you sure you want Accept " + Checked_List.size() + " Invoice for " /*+ months_all[month - 1] + " " + String.valueOf(year)*/ + "?");
                    alertDialogBuilder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int arg1) {
                                    dialog.dismiss();

                                    //AcceptReconciliationReport();
                                }
                            });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });

        invMismatchReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checked_List.clear();
                ButtonStatus = "Reject";
                for (int i = 0; i < mismatchReportList.size(); i++) {
                    if (mismatchReportList.get(i).isSelectcheckbox()) {
                        Checked_List.add(mismatchReportList.get(i));
                    }
                }
                if (Checked_List.size() == 0) {
                    Toast.makeText(ReconGSTR2Activity.this, "Please Select Invoice", Toast.LENGTH_SHORT).show();
                } else {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReconGSTR2Activity.this);
                    alertDialogBuilder.setMessage("Are you sure you want Reject " + Checked_List.size() + " Invoice for " /*+ months_all[month - 1] + " " + String.valueOf(year)*/ + "?");
                    alertDialogBuilder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int arg1) {
                                    dialog.dismiss();
                                    //AcceptReconciliationReport();
                                }
                            });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });

        inGstr2aAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checked_List.clear();
                ButtonStatus = "Accept";
                for (int i = 0; i < mismatchReportList.size(); i++) {
                    if (mismatchReportList.get(i).isSelectcheckbox()) {
                        Checked_List.add(mismatchReportList.get(i));
                    }
                }
                if (Checked_List.size() == 0) {
                    Toast.makeText(ReconGSTR2Activity.this, "Please Select  Invoice", Toast.LENGTH_SHORT).show();
                } else {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReconGSTR2Activity.this);
                    alertDialogBuilder.setMessage("Are you sure you want Accept " + Checked_List.size() + " Invoice for " /*+ months_all[month - 1] + " " + String.valueOf(year) */+ "?");
                    alertDialogBuilder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int arg1) {
                                    dialog.dismiss();

                                    //AcceptReconciliationReport();
                                }
                            });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });

        /*inGstr2aManualMatchingPR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReconGSTR2Activity.this,ReconGSTR2ManualMatchingSellerActivity.class);
                Bundle b = new Bundle();
                b.putString("date",fullDate);
                i.putExtras(b);
                startActivity(i);
            }
        });*/

        inPrAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checked_List.clear();
                ButtonStatus = "prAccept";
                for (int i = 0; i < mismatchReportList.size(); i++) {
                    if (mismatchReportList.get(i).isSelectcheckbox()) {
                        Checked_List.add(mismatchReportList.get(i));
                    }
                }
                if (Checked_List.size() == 0) {
                    Toast.makeText(ReconGSTR2Activity.this, "Please Select Invoice", Toast.LENGTH_SHORT).show();
                } else {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReconGSTR2Activity.this);
                    alertDialogBuilder.setMessage("Are you sure you want Accept " + Checked_List.size() + " Invoice?"/* for " + months_all[month - 1] + " " + String.valueOf(year) + "?"*/);
                    alertDialogBuilder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int arg1) {
                                    dialog.dismiss();

                                    //AcceptReconciliationReport();
                                }
                            });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });

        inPrUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Checked_List.clear();
                ButtonStatus = "Undo";
                for (int i = 0; i < mismatchReportList.size(); i++) {
                    if (mismatchReportList.get(i).isSelectcheckbox()) {
                        Checked_List.add(mismatchReportList.get(i));
                    }
                }
                if (Checked_List.size() == 0) {
                    Toast.makeText(ReconGSTR2Activity.this, "Please Select Invoice", Toast.LENGTH_SHORT).show();
                } else {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReconGSTR2Activity.this);
                    alertDialogBuilder.setMessage(Checked_List.size() + " Invoice selected. Are you sure you want Undo?");// Invoices for " + dateFromTo[0] + " " + dateFromTo[1] + " - " + dateFromTo[2] + " " + dateFromTo[3] + "?");
                    alertDialogBuilder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int arg1) {
                                    dialog.dismiss();

                                    //undoReconciliation();
                                }
                            });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });


        /*inPrmanualMatching2A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReconGSTR2Activity.this,ReconGSTR2ManualMatchingSellerActivity.class);
                Bundle b = new Bundle();
                b.putString("date",fullDate);
                i.putExtras(b);
                startActivity(i);
            }
        });*/


        getReconciliationReport("27AAACC1645G1ZZ", "07", "2017","08","2017");
        rvAllInvoice.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1))
                    onScrolledToBottom();
            }
        });

        mismatchAdapter = new MismatchAdapter(ReconGSTR2Activity.this, tempMisMatchList, flag, type,progress);
        rvAllInvoice.setAdapter(mismatchAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return false;
    }

    private void getReconciliationReport(String buyerGstin, String fromMonth, String fromYear,String toMonth, String toYear) {

//        getGstr2ASessionProgressDialog = new SpotsDialog(ReconGSTR2Activity.this, R.style.Custom);
//        getGstr2ASessionProgressDialog.setCancelable(false);
//        getGstr2ASessionProgressDialog.show();
        data.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        buyerGstin = Helper.encodeURL(buyerGstin);
        fromMonth = Helper.encodeURL(fromMonth);
        fromYear = Helper.encodeURL(fromYear);
        toMonth = Helper.encodeURL(toMonth);
        toYear = Helper.encodeURL(toYear);
       // http://35.154.23.209:8080/gstsvrc-0.0.1/getGstr2APurchaseReconciliationReportBasedOnType?companyId=CM00000931&buyerGstin=07AAACB0446L1ZU&fromYear=2017&fromMonth=07&toYear=2017&toMonth=07&type=MATCHED
        String url= "";
        if(gstin.equalsIgnoreCase("")) {
            url = Uri.parse(IPADDRESS+"getGstr2APurchaseReconciliationReportBasedOnType")
                    .buildUpon()
                    .appendQueryParameter("companyId","CM00000931")
                    .appendQueryParameter("buyerGstin", "07AAACB0446L1ZU")
                    .appendQueryParameter("fromYear", "2017")
                    .appendQueryParameter("fromMonth", "08")
                    .appendQueryParameter("toYear", "2017")
                    .appendQueryParameter("toMonth", "08")
                    .appendQueryParameter("type", "MATCHED")
                    .build().toString();
        }else
        {
            url = Uri.parse(IPADDRESS+"getGstr2APurchaseReconciliationReportBasedOnType")
                    .buildUpon()
                    .appendQueryParameter("companyId","CM00000931")
                    .appendQueryParameter("buyerGstin", "07AAACB0446L1ZU")
                    .appendQueryParameter("fromYear", "2017")
                    .appendQueryParameter("fromMonth", "08")
                    .appendQueryParameter("toYear", "2017")
                    .appendQueryParameter("toMonth", "08")
                    .appendQueryParameter("type", "MATCHED")
                    .build().toString();
        }
        Helper.Log(TAG, url);

        data.setVisibility(View.GONE);
        rr_NoConnection.setVisibility(View.GONE);


        if (homeConnDetector.isConnectingToInternet()) {

            StringRequest salInvGetReq = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Helper.Log(TAG, response);
                            writeLogCat();
                            /*int maxLogSize = 1000;
                            for(int i = 0; i <= response.length() / maxLogSize; i++) {
                                int start = i * maxLogSize;
                                int end = (i+1) * maxLogSize;
                                end = end > response.length() ? response.length() : end;
                                Helper.Log(TAG, response.substring(start, end));
                            }*/
                            /*if (response.length() > 4000) {
                                Helper.Log(TAG, "sb.length = " + response.length());
                                int chunkCount = response.length() / 4000;     // integer division
                                for (int i = 0; i <= chunkCount; i++) {
                                    int max = 4000 * (i + 1);
                                    if (max >= response.length()) {
                                        Helper.Log(TAG, "chunk " + i + " of " + chunkCount + ":" + response.substring(4000 * i));
                                    } else {
                                        Helper.Log(TAG, "chunk " + i + " of " + chunkCount + ":" + response.substring(4000 * i, max));
                                    }
                                }
                            }*/

                            progress.setVisibility(View.VISIBLE);
                            mHandler.sendEmptyMessage(MSG_START_TIMER);
                            try {
                                //Parsing data
                                double totalGInvoice = 0, totalPInvoice = 0, totalGTaxable = 0, totalPTaxable = 0;

                                String sellerGstin = "";
                                String sellerState = "";
                                String invoiceNumber = "";

                                JSONObject obj = new JSONObject(response);
                           //     JSONObject res= new JSONObject(respon);
//                                JSONArray jsonarrya = new JSONArray(res.getString("summary"));
//                                for (int i = 0; i < jsonarrya.length(); i++) {
//
//
//                                    JSONObject c = jsonarrya.getJSONObject(i);
//
//                                    String validationStatus = c.getString("validationStatus");
//                                    String prInv = Helper.nullcheckDouble(c.getString("prInv"));
//                                    String prTaxVal = Helper.nullcheckDouble((c.getString("prTaxVal")));
//                                    String g2aInv = Helper.nullcheckDouble(c.getString("g2aInv"));
//                                    String g2aTaxVal = Helper.nullcheckDouble(c.getString("g2aTaxVal"));
//                                    String rowType = c.getString("rowType");
//
//                                    if (type.equalsIgnoreCase("InPR")) {
//
//                                        if (validationStatus.equalsIgnoreCase("UNMATCHED")) {
//                                            if (rowType.equalsIgnoreCase("2A")) {
//                                                totalGInvoice = Double.parseDouble(g2aInv);
//                                                totalGTaxable = Double.parseDouble(g2aTaxVal);
//                                            } else {
//                                                totalPInvoice = Double.parseDouble(prInv);
//                                                totalPTaxable = Double.parseDouble(prTaxVal);
//                                            }
//                                        }
//                                    } else if (type.equalsIgnoreCase("InGS")) {
//                                        if (validationStatus.equalsIgnoreCase("UNMATCHED")) {
//                                            if (rowType.equalsIgnoreCase("2A")) {
//                                                totalGInvoice = Double.parseDouble(g2aInv);
//                                                totalGTaxable = Double.parseDouble(g2aTaxVal);
//                                            } else {
//                                                totalPInvoice = Double.parseDouble(prInv);
//                                                totalPTaxable = Double.parseDouble(prTaxVal);
//                                            }
//
//                                        }
//
//                                    } else if (type.equalsIgnoreCase("Match")) {
//
//                                        if (validationStatus.equalsIgnoreCase("MATCHED")) {
//
//                                            totalGInvoice = Double.parseDouble(g2aInv);
//                                            totalGTaxable = Double.parseDouble(g2aTaxVal);
//
//                                            totalPInvoice = Double.parseDouble(prInv);
//                                            totalPTaxable = Double.parseDouble(prTaxVal);
//
//                                        }
//                                    } else if (type.equalsIgnoreCase("MisMatch")) {
//                                        if (validationStatus.equalsIgnoreCase("MISMATCHED")) {
//
//                                            totalGInvoice = Double.parseDouble(g2aInv);
//                                            totalGTaxable = Double.parseDouble(g2aTaxVal);
//
//                                            totalPInvoice = Double.parseDouble(prInv);
//                                            totalPTaxable = Double.parseDouble(prTaxVal);
//                                        }
//                                    }
//                                    else if (type.equalsIgnoreCase("INV_MISMAT")) {
//                                        if (validationStatus.equalsIgnoreCase("INV_MISMAT")) {
//
//                                            totalGInvoice = Double.parseDouble(g2aInv);
//                                            totalGTaxable = Double.parseDouble(g2aTaxVal);
//
//                                            totalPInvoice = Double.parseDouble(prInv);
//                                            totalPTaxable = Double.parseDouble(prTaxVal);
//                                        }
//                                    }
//                                    else if (type.equalsIgnoreCase("ALL_Records")) {
//
//                                    }
//                                }

                                if (type.equalsIgnoreCase("ALL_Records")) {
                                    //Seller Summary
                                    progress.setVisibility(View.VISIBLE);
                                    ll_all_summary.setVisibility(View.VISIBLE);
                                    //Matched
                                    matched_no_of_invoices.setText("0");
                                    matched_invoice_value.setText("₹0");
                                    matched_taxable_value.setText("₹0");
                                    matched_tax_value.setText("₹0");
                                    //Mismatched
                                    mismatched_no_of_invoices.setText("0");
                                    mismatched_invoice_value.setText("₹0");
                                    mismatched_taxable_value.setText("₹0");
                                    mismatched_tax_value.setText("₹0");
                                    //Invoice Mismatched
                                    invMismatched_no_of_invoices.setText("0");
                                    invMismatched_invoice_value.setText("₹0");
                                    invMismatched_taxable_value.setText("₹0");
                                    invMismatched_tax_value.setText("₹0");
                                    //Gstr2a
                                    gstr2a_no_of_invoices.setText("0");
                                    gstr2a_invoice_value.setText("₹0");
                                    gstr2a_taxable_value.setText("₹0");
                                    gstr2a_tax_value.setText("₹0");
                                    //Pr
                                    pr_no_of_invoices.setText("0");
                                    pr_invoice_value.setText("₹0");
                                    pr_taxable_value.setText("₹0");
                                    pr_tax_value.setText("₹0");
                                    pr_tax_value.setText("₹0");
                                    //Total
                                    recon_total_no_of_invoices.setText("0");
                                    recon_total_invoice_value.setText("₹0");
                                    recon_total_taxable_value.setText("₹0");
                                    recon_total_tax_value.setText("₹0");
                                    recon_total_tax_value.setText("₹0");

                                    JSONArray jsonarrySummary = new JSONArray(obj.getString("sellerSummarry"));
                                    for (int i = 0; i < jsonarrySummary.length(); i++) {

                                        switch (jsonarrySummary.getJSONObject(i).getString("validationStatus")) {
                                            case "MATCHED":
                                                matched_no_of_invoices.setText(jsonarrySummary.getJSONObject(i).getString("g2aCount"));
                                                matched_invoice_value.setText(String.format("₹%s", Helper.formatAmount(Double.parseDouble(jsonarrySummary.getJSONObject(i).getString("g2aInv")))));
                                                matched_taxable_value.setText(String.format("₹%s", Helper.formatAmount(Double.parseDouble(jsonarrySummary.getJSONObject(i).getString("g2aTaxVal")))));
                                                matched_tax_value.setText(String.format("₹%s", Helper.formatAmount(Double.parseDouble(jsonarrySummary.getJSONObject(i).getString("g2aTax")))));
                                                break;
                                            case "MISMATCHED":
                                                mismatched_no_of_invoices.setText(jsonarrySummary.getJSONObject(i).getString("g2aCount"));
                                                mismatched_invoice_value.setText(String.format("₹%s", Helper.formatAmount(Double.parseDouble(jsonarrySummary.getJSONObject(i).getString("g2aInv")))));
                                                mismatched_taxable_value.setText(String.format("₹%s", Helper.formatAmount(Double.parseDouble(jsonarrySummary.getJSONObject(i).getString("g2aTaxVal")))));
                                                mismatched_tax_value.setText(String.format("₹%s", Helper.formatAmount(Double.parseDouble(jsonarrySummary.getJSONObject(i).getString("g2aTax")))));
                                                break;
                                            case "INV_MISMAT":
                                                invMismatched_no_of_invoices.setText(jsonarrySummary.getJSONObject(i).getString("g2aCount"));
                                                invMismatched_invoice_value.setText(String.format("₹%s", Helper.formatAmount(Double.parseDouble(jsonarrySummary.getJSONObject(i).getString("g2aInv")))));
                                                invMismatched_taxable_value.setText(String.format("₹%s", Helper.formatAmount(Double.parseDouble(jsonarrySummary.getJSONObject(i).getString("g2aTaxVal")))));
                                                invMismatched_tax_value.setText(String.format("₹%s", Helper.formatAmount(Double.parseDouble(jsonarrySummary.getJSONObject(i).getString("g2aTax")))));
                                                break;
                                            case "UNMATCHED":
                                                if (jsonarrySummary.getJSONObject(i).getString("rowType").equalsIgnoreCase("2a")) {
                                                    gstr2a_no_of_invoices.setText(jsonarrySummary.getJSONObject(i).getString("g2aCount"));
                                                    gstr2a_invoice_value.setText(String.format("₹%s", Helper.formatAmount(Double.parseDouble(jsonarrySummary.getJSONObject(i).getString("g2aInv")))));
                                                    gstr2a_taxable_value.setText(String.format("₹%s", Helper.formatAmount(Double.parseDouble(jsonarrySummary.getJSONObject(i).getString("g2aTaxVal")))));
                                                    gstr2a_tax_value.setText(String.format("₹%s", Helper.formatAmount(Double.parseDouble(jsonarrySummary.getJSONObject(i).getString("g2aTax")))));
                                                } else {
                                                    pr_no_of_invoices.setText(jsonarrySummary.getJSONObject(i).getString("prCount"));
                                                    pr_invoice_value.setText(String.format("₹%s", Helper.formatAmount(Double.parseDouble(jsonarrySummary.getJSONObject(i).getString("prInv")))));
                                                    pr_taxable_value.setText(String.format("₹%s", Helper.formatAmount(Double.parseDouble(jsonarrySummary.getJSONObject(i).getString("prTaxVal")))));
                                                    pr_tax_value.setText(String.format("₹%s", Helper.formatAmount(Double.parseDouble(jsonarrySummary.getJSONObject(i).getString("prTax")))));
                                                }
                                                break;
                                            case "ALL":
                                                recon_total_no_of_invoices.setText(String.valueOf(Integer.parseInt(jsonarrySummary.getJSONObject(i).getString("g2aCount")) +
                                                        Integer.parseInt(jsonarrySummary.getJSONObject(i).getString("prCount"))));
                                                recon_total_invoice_value.setText(String.format("₹%s", Helper.formatAmount(Double.parseDouble(jsonarrySummary.getJSONObject(i).getString("g2aInv")) +
                                                        Double.parseDouble(jsonarrySummary.getJSONObject(i).getString("prInv")))));
                                                recon_total_taxable_value.setText(String.format("₹%s", Helper.formatAmount(Double.parseDouble(jsonarrySummary.getJSONObject(i).getString("g2aTaxVal")) +
                                                        Double.parseDouble(jsonarrySummary.getJSONObject(i).getString("prTaxVal")))));
                                                recon_total_tax_value.setText(String.format("₹%s", Helper.formatAmount(Double.parseDouble(jsonarrySummary.getJSONObject(i).getString("g2aTax")) +
                                                        Double.parseDouble(jsonarrySummary.getJSONObject(i).getString("prTax")))));
                                                break;
                                        }
                                    }

                                }else
                                {
                                    ll_all_summary.setVisibility(View.GONE);
                                }

                                //Recon Array
                                JSONArray jsonarry = new JSONArray(obj.getString("reco"));
                                /*InputStream inputStream = new ByteArrayInputStream(response.getBytes(Charset.forName("UTF-8")));
                                InputStreamReader reader = new InputStreamReader(inputStream);
                                Gson gson = new Gson();
                                MismatchReportArrayGson itemList = gson.fromJson(reader, MismatchReportArrayGson.class);
                                mismatchReportListGson = itemList.getReco();


                                for(int i=0;i<mismatchReportListGson.size();i++)
                                {
                                    if (mismatchReportListGson.get(i).getRowType().equalsIgnoreCase("2A")) {
                                        mismatchReportListGson.get(i).setGSTIN_no(mismatchReportListGson.get(i).getGs_sellerGSTIN());
                                        mismatchReportListGson.get(i).setState(mismatchReportListGson.get(i).getGs_pos());
                                        mismatchReportListGson.get(i).setIvNo(mismatchReportListGson.get(i).getGs_ivNo());
                                    }else{
                                        mismatchReportListGson.get(i).setGSTIN_no(mismatchReportListGson.get(i).getAb_sellerGSTIN());
                                        mismatchReportListGson.get(i).setState(mismatchReportListGson.get(i).getAb_pos());
                                        mismatchReportListGson.get(i).setIvNo(mismatchReportListGson.get(i).getAb_ivNo());
                                    }
                                }

                                Helper.Log(TAG,mismatchReportListGson.toString());
                                Helper.Log(TAG,String.valueOf(mismatchReportListGson.size()));*/

                                int totalInvoice = 0, totalMatched = 0, totalRecon = 0;
                                int all = 0, matchedCount = 0, mismatchedCount = 0, inAbnotGs = 0, inGsnotAb = 0;
                                countReason =0;

                                //ArrayList<MismatchReports> localData = new ArrayList();

                                mismatchReportList = new ArrayList<>();
                                for (int i = 0; i < jsonarry.length(); i++) {
                                    String g2a_invoiceNo ="",gInvoiceDate ="",g2a_typeofinvoice ="",g2a_reverseCharge="",g2a_financialPeriod="",g2a_pos="",
                                            gInvoiceAmount="0.0",gTaxableAmount="0.0",gTaxAmount="0.0",gIgst="0.0",gCgst="0.0",gSgst="0.0",gCess="0.0",
                                            gRefInvNo ="",gRefInvDate ="",gRefCdnReason ="";

                                    String pr_invoiceNo ="",pInvoiceDate ="",pr_typeofinvoice ="",pr_reverseCharge="",pr_financialPeriod="",pr_pos="",
                                            pInvoiceAmount="0.0",pTaxableAmount="0.0",pTaxAmount="0.0",pIgst="0.0",pCgst="0.0",pSgst="0.0",pCess="0.0",
                                            pRefInvNo ="",pRefInvDate ="",pRefCdnReason ="",pInvoiceType="";


                                    JSONObject c = jsonarry.getJSONObject(i);
                                    String rowType = c.getString("rowType");

                                    if (rowType.equalsIgnoreCase("2A")) {
                                        sellerGstin = c.getString("g2a_sellerGSTIN");
                                        sellerState = c.getString("g2a_pos");
                                        invoiceNumber = c.getString("g2a_invoiceNo");

                                        //GSTR 2A
                                        g2a_invoiceNo = Helper.nullcheckString(c.getString("g2a_invoiceNo"));
                                        gInvoiceDate = Helper.nullcheckString(c.getString("g2a_invoiceDate"));
                                        g2a_typeofinvoice = Helper.nullcheckString(c.getString("g2a_typeofinvoice"));
                                        g2a_reverseCharge = Helper.nullcheckString(c.getString("g2a_reverseCharge"));
                                        g2a_financialPeriod =  Helper.nullcheckString(c.getString("g2a_financialPeriod"));
                                        g2a_pos = Helper.nullcheckString(c.getString("g2a_pos"));

                                        gInvoiceAmount = Helper.nullcheckDouble(c.getString("g2a_invoiceValue"));
                                        gTaxableAmount = Helper.nullcheckDouble(c.getString("g2a_taxableValue"));
                                        gTaxAmount = Helper.nullcheckDouble(c.getString("g2a_taxValue"));
                                        gIgst = Helper.nullcheckDouble(c.getString("g2a_igstAmt"));
                                        gCgst = Helper.nullcheckDouble(c.getString("g2a_cgstAmt"));
                                        gSgst = Helper.nullcheckDouble(c.getString("g2a_sgstAmt"));
                                        gCess = Helper.nullcheckDouble(c.getString("g2a_cessAmt"));

                                        gRefInvNo = Helper.nullcheckString(c.getString("g2a_refrInvoiceNo"));
                                        gRefInvDate = Helper.nullcheckString(c.getString("g2a_refrInvoiceDate"));
                                        gRefCdnReason = Helper.nullcheckString(c.getString("g2a_cdnReason"));



                                    }else if (rowType.equalsIgnoreCase("PR")) {
                                        sellerGstin = c.getString("pr_sellerGSTIN");
                                        sellerState = c.getString("pr_pos");
                                        invoiceNumber = c.getString("pr_invoiceNo");

                                        //Purchase Register
                                        pr_invoiceNo = Helper.nullcheckString(c.getString("pr_invoiceNo"));
                                        pInvoiceDate = Helper.nullcheckString(c.getString("pr_invoiceDate"));
                                        pr_typeofinvoice = Helper.nullcheckString(c.getString("pr_typeofinvoice"));
                                        pr_reverseCharge = Helper.nullcheckString(c.getString("pr_reverseCharge"));
                                        pr_financialPeriod =  Helper.nullcheckString(c.getString("pr_financialPeriod"));
                                        pr_pos = Helper.nullcheckString(c.getString("pr_pos"));

                                        pInvoiceAmount = Helper.nullcheckDouble(c.getString("pr_invoiceValue"));
                                        pTaxableAmount = Helper.nullcheckDouble(c.getString("pr_taxableValue"));
                                        pTaxAmount = Helper.nullcheckDouble(c.getString("pr_taxValue"));
                                        pInvoiceType = c.getString("pr_invoiceSubType");
                                        if (!pr_typeofinvoice.equalsIgnoreCase("B2B")) {
                                            pr_typeofinvoice = "CNR";
                                        }
                                        pIgst = Helper.nullcheckDouble(c.getString("pr_igstAmt"));
                                        pCgst = Helper.nullcheckDouble(c.getString("pr_cgstAmt"));
                                        pSgst = Helper.nullcheckDouble(c.getString("pr_sgstAmt"));
                                        pCess = Helper.nullcheckDouble(c.getString("pr_cessAmt"));

                                        pRefInvNo = Helper.nullcheckString(c.getString("pr_refrInvoiceNo"));
                                        pRefInvDate = Helper.nullcheckString(c.getString("pr_refrInvoiceDate"));
                                        pRefCdnReason = Helper.nullcheckString(c.getString("pr_cdnReason"));
                                    } else if (rowType.equalsIgnoreCase("PR2A")) {

                                        sellerGstin = c.getString("g2a_sellerGSTIN");
                                        sellerState = c.getString("g2a_pos");
                                        invoiceNumber = c.getString("g2a_invoiceNo");

                                        //GSTR 2A
                                        g2a_invoiceNo = Helper.nullcheckString(c.getString("g2a_invoiceNo"));
                                        gInvoiceDate = Helper.nullcheckString(c.getString("g2a_invoiceDate"));
                                        g2a_typeofinvoice = Helper.nullcheckString(c.getString("g2a_typeofinvoice"));
                                        g2a_reverseCharge = Helper.nullcheckString(c.getString("g2a_reverseCharge"));
                                        g2a_financialPeriod =  Helper.nullcheckString(c.getString("g2a_financialPeriod"));
                                        g2a_pos = Helper.nullcheckString(c.getString("g2a_pos"));

                                        gInvoiceAmount = Helper.nullcheckDouble(c.getString("g2a_invoiceValue"));
                                        gTaxableAmount = Helper.nullcheckDouble(c.getString("g2a_taxableValue"));
                                        gTaxAmount = Helper.nullcheckDouble(c.getString("g2a_taxValue"));
                                        gIgst = Helper.nullcheckDouble(c.getString("g2a_igstAmt"));
                                        gCgst = Helper.nullcheckDouble(c.getString("g2a_cgstAmt"));
                                        gSgst = Helper.nullcheckDouble(c.getString("g2a_sgstAmt"));
                                        gCess = Helper.nullcheckDouble(c.getString("g2a_cessAmt"));

                                        gRefInvNo = Helper.nullcheckString(c.getString("g2a_refrInvoiceNo"));
                                        gRefInvDate = Helper.nullcheckString(c.getString("g2a_refrInvoiceDate"));
                                        gRefCdnReason = Helper.nullcheckString(c.getString("g2a_cdnReason"));

                                        //Purchase Register
                                        pr_invoiceNo = Helper.nullcheckString(c.getString("pr_invoiceNo"));
                                        pInvoiceDate = Helper.nullcheckString(c.getString("pr_invoiceDate"));
                                        pr_typeofinvoice = Helper.nullcheckString(c.getString("pr_typeofinvoice"));
                                        pr_reverseCharge = Helper.nullcheckString(c.getString("pr_reverseCharge"));
                                        pr_financialPeriod =  Helper.nullcheckString(c.getString("pr_financialPeriod"));
                                        pr_pos = Helper.nullcheckString(c.getString("pr_pos"));

                                        pInvoiceAmount = Helper.nullcheckDouble(c.getString("pr_invoiceValue"));
                                        pTaxableAmount = Helper.nullcheckDouble(c.getString("pr_taxableValue"));
                                        pTaxAmount = Helper.nullcheckDouble(c.getString("pr_taxValue"));
                                        pInvoiceType = c.getString("pr_invoiceSubType");
                                        if (!pr_typeofinvoice.equalsIgnoreCase("B2B")) {
                                            pr_typeofinvoice = "CNR";
                                        }
                                        pIgst = Helper.nullcheckDouble(c.getString("pr_igstAmt"));
                                        pCgst = Helper.nullcheckDouble(c.getString("pr_cgstAmt"));
                                        pSgst = Helper.nullcheckDouble(c.getString("pr_sgstAmt"));
                                        pCess = Helper.nullcheckDouble(c.getString("pr_cessAmt"));

                                        pRefInvNo = Helper.nullcheckString(c.getString("pr_refrInvoiceNo"));
                                        pRefInvDate = Helper.nullcheckString(c.getString("pr_refrInvoiceDate"));
                                        pRefCdnReason = Helper.nullcheckString(c.getString("pr_cdnReason"));
                                    }

                                    String reconType = c.getString("reconType");
                                    String reconRemark =  Helper.nullcheckString(c.getString("reconRemark"));
                                    String mismatchColumns =  Helper.nullcheckString(c.getString("mismatchColumns"));


                                    //General Details
                                    String legalNameOfBusiness =  Helper.nullcheckString(c.getString("legalNameOfBusiness"));
                                    String financialPeriod =  Helper.nullcheckString(c.getString("financialPeriod"));
                                    String tradeName =  Helper.nullcheckString(c.getString("tradeName"));

                                    //  String color = c.getString("color");
                                    String status = c.getString("validationStatus");

                                    if (status.equalsIgnoreCase("null")) {
                                        status = "";
                                    }

                                    String pFlag = c.getString("pr_flag");
                                    if (pFlag.equalsIgnoreCase("null")) {
                                        pFlag = "";
                                    }
                                    if(!(reconType.equalsIgnoreCase("")||
                                            reconType.equalsIgnoreCase("Auto Match") ||
                                            reconType.equalsIgnoreCase("Round off")))
                                        countReason++;

                                    MismatchReports mismatchReports = new MismatchReports(sellerGstin, legalNameOfBusiness, tradeName,sellerState, invoiceNumber,
                                            g2a_invoiceNo,g2a_financialPeriod,g2a_typeofinvoice,g2a_reverseCharge,g2a_pos,
                                            gInvoiceDate, gInvoiceAmount, gTaxableAmount, gTaxAmount,gIgst, gCgst, gSgst, gCess,
                                            gRefInvNo,gRefInvDate,gRefCdnReason,
                                            pr_invoiceNo,pr_financialPeriod,pr_typeofinvoice,pr_reverseCharge,pr_pos,
                                            pInvoiceDate, pInvoiceAmount, pTaxableAmount,pTaxAmount, pIgst, pCgst, pSgst, pCess,
                                            pRefInvNo,pRefInvDate,pRefCdnReason,reconRemark,mismatchColumns,
                                            "", status,rowType,reconType, "", pFlag, pr_typeofinvoice,pInvoiceType);
                                    mismatchReportList.add(mismatchReports);
                                    //localData.add(mismatchReports);

                                    totalInvoice++;
                                    if (status.equalsIgnoreCase("MATCHED"))
                                        totalMatched++;


                                }

                                tempMisMatchList = new ArrayList<>();
                                tempMisMatchList.addAll(mismatchReportList);

                                /*for(int i=0;i<mismatchReportList.size();i++) {
                                    if (mismatchReportList.get(i).getReconType().equals("MATCHED"))
                                        tempMisMatchList.add(mismatchReportList.get(i));
                                    //tempMisMatchList.addAll(mismatchReportList);
                                }
                                for(int i=0;i<mismatchReportList.size();i++) {
                                    if (mismatchReportList.get(i).getReconType().equals("MISMATCHED"))
                                        tempMisMatchList.add(mismatchReportList.get(i));
                                }

                                for(int i=0;i<mismatchReportList.size();i++) {
                                    if (mismatchReportList.get(i).getReconType().equals("INV_MISMAT"))
                                        tempMisMatchList.add(mismatchReportList.get(i));
                                }

                                for(int i=0;i<mismatchReportList.size();i++) {
                                    if (mismatchReportList.get(i).getReconType().equals("UNMATCHED") &&
                                            mismatchReportList.get(i).getRowType().equals("2A"))
                                        tempMisMatchList.add(mismatchReportList.get(i));
                                }

                                for(int i=0;i<mismatchReportList.size();i++) {
                                    if (mismatchReportList.get(i).getReconType().equals("UNMATCHED") &&
                                            mismatchReportList.get(i).getRowType().equals("PR"))
                                        tempMisMatchList.add(mismatchReportList.get(i));
                                }*/

                                // mismatchReportList=new ArrayList<>();
                                //mismatchReportList.addAll(tempMisMatchList);


                                if (type.equalsIgnoreCase("InPR")) {
                                    total_invoice_value.setText("₹" + Helper.format2DecAmountDouble(totalPInvoice));
                                    total_taxable_value.setText("₹" + Helper.format2DecAmountDouble(totalPTaxable));
                                } else {
                                    total_invoice_value.setText("₹" + Helper.format2DecAmountDouble(totalGInvoice));
                                    total_taxable_value.setText("₹" + Helper.format2DecAmountDouble(totalGTaxable));
                                }
                                gstr2a_total_invoice_value.setText("₹" + Helper.format2DecAmountDouble(totalGInvoice));
                                gstr2a_total_taxable_value.setText("₹" + Helper.format2DecAmountDouble(totalGTaxable));
                                purchase_total_invoice_value.setText("₹" + Helper.format2DecAmountDouble(totalPInvoice));
                                purchase_total_taxable_value.setText("₹" + Helper.format2DecAmountDouble(totalPTaxable));

                                if (tempMisMatchList.size() == 0) {

                                    //checkAll.setTitle("Select All");
                                    Toast.makeText(ReconGSTR2Activity.this, "No Data Found", Toast.LENGTH_LONG).show();
                                    rvAllInvoice.setVisibility(View.GONE);
                                    nodate.setVisibility(View.VISIBLE);
                                    inGstr2a.setVisibility(View.GONE);
                                    invMismatch.setVisibility(View.GONE);
                                    mismatch.setVisibility(View.GONE);

                                    //setActionItems(View.GONE,false);

                                } else {
                                   // getGstr2ASessionProgressDialog.show();

                                    mismatchAdapter = new MismatchAdapter(ReconGSTR2Activity.this, tempMisMatchList, flag, type,progress);
                                    rvAllInvoice.setAdapter(mismatchAdapter);
                                    rvAllInvoice.setVisibility(View.VISIBLE);
                                    nodate.setVisibility(View.GONE);
                                    progress.setVisibility(View.GONE);
                                    data.setVisibility(View.VISIBLE);
                                    //setActionItems(View.VISIBLE,true);
                                }

                                if (type.equalsIgnoreCase("Match")) {
                                    if (countReason == 0)
                                        checkAll.setVisible(false);
                                }

                                ChangeHeaderName();

                                mHandler.sendEmptyMessage(MSG_STOP_TIMER);


                            } catch (Exception e) {

                                Helper.Log(TAG, "Error " +  " :" + e);
                                e.printStackTrace();
                            } finally {

                            }
                           // getGstr2ASessionProgressDialog.dismiss();
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    try {
                        if (getGstr2ASessionProgressDialog.isShowing())
                            getGstr2ASessionProgressDialog.dismiss();

                        rr_NoConnection.setVisibility(View.VISIBLE);
                        if (error instanceof NetworkError) {
                            //Toast.makeText(getContext(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
                            Helper.Log(TAG, getResources().getString(R.string.NetworkError));
                        } else if (error instanceof ServerError) {
                            //Toast.makeText(getContext(), "The server could not be found. Please try again after some time!!", Toast.LENGTH_LONG).show();
                            Helper.Log(TAG, getResources().getString(R.string.ServerError));
                        } else if (error instanceof AuthFailureError) {
                            //Toast.makeText(getContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG).show();
                            Helper.Log(TAG, getResources().getString(R.string.AuthFailureError));
                        } else if (error instanceof ParseError) {
                            //Toast.makeText(getContext(), "Parsing error! Please try again after some time!!", Toast.LENGTH_LONG).show();
                            Helper.Log(TAG, getResources().getString(R.string.NetworkError));
                        } else if (error instanceof TimeoutError) {
                            //Toast.makeText(getContext(), "Connection TimeOut! Please check your internet connection.", Toast.LENGTH_LONG).show();
                            Helper.Log(TAG, getResources().getString(R.string.TimeoutError));
                        }

                        Helper.Log(TAG, "Error: " + error);


                    } catch (Exception e) {
                        Helper.sendCrashReportDetails(ReconGSTR2Activity.this,e.getMessage(),e.getStackTrace());

                    }

                }

            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("empID", "UID0000993");
                    return headers;
                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(salInvGetReq);
        } else {
            rvAllInvoice.setVisibility(View.GONE);
            nodate.setVisibility(View.GONE);
            inGstr2a.setVisibility(View.GONE);
            invMismatch.setVisibility(View.GONE);
            mismatch.setVisibility(View.GONE);
//            if (getGstr2ASessionProgressDialog.isShowing())
//                getGstr2ASessionProgressDialog.dismiss();
            rr_NoConnection.setVisibility(View.VISIBLE);
            list.setText(getResources().getString(R.string.noInternet));
            //Toast.makeText(ReconGSTR2Activity.this, getResources().getString(R.string.noInternet), Toast.LENGTH_LONG).show();
        }
    }

    private void ChangeHeaderName() {

        ViewGroup.MarginLayoutParams marginLayoutParams =
                (ViewGroup.MarginLayoutParams) data.getLayoutParams();
        if (dm.heightPixels < 1900) {
            marginLayoutParams.setMargins(0, 0, 0, 80);
        } else {
            marginLayoutParams.setMargins(0, 0, 0, 150);
        }

        if (type.equalsIgnoreCase("Match")) {
            getSupportActionBar().setTitle("Matched");
            rl_single.setVisibility(View.VISIBLE);
            rl_double.setVisibility(View.GONE);

            match.setVisibility(View.VISIBLE);
            invMismatch.setVisibility(View.GONE);
            mismatch.setVisibility(View.GONE);
            inGstr2a.setVisibility(View.GONE);
            inPR.setVisibility(View.GONE);
           /* if(recordCount==0)
                marginLayoutParams.setMargins(0, 0, 0, 0);*/

        } else if (type.equalsIgnoreCase("INV_MISMAT")) {
            getSupportActionBar().setTitle("Invoice Mismatch");
            rl_single.setVisibility(View.GONE);
            rl_double.setVisibility(View.VISIBLE);

            checkAll.setVisible(true);
            match.setVisibility(View.GONE);
            invMismatch.setVisibility(View.VISIBLE);
            mismatch.setVisibility(View.GONE);
            inGstr2a.setVisibility(View.GONE);
            inPR.setVisibility(View.GONE);

        }else if (type.equalsIgnoreCase("MisMatch")) {
            getSupportActionBar().setTitle("Mismatched");
            rl_single.setVisibility(View.GONE);
            rl_double.setVisibility(View.VISIBLE);

            checkAll.setVisible(true);
            match.setVisibility(View.GONE);
            invMismatch.setVisibility(View.GONE);
            mismatch.setVisibility(View.VISIBLE);
            inGstr2a.setVisibility(View.GONE);
            inPR.setVisibility(View.GONE);

        }else if (type.equalsIgnoreCase("InGS")) {
            getSupportActionBar().setTitle("In GSTR2A");
            total_invoice_text.setText("GSTR-2A Total Invoice");
            total_taxable_text.setText("GSTR-2A Total Taxable");
            rl_single.setVisibility(View.VISIBLE);
            rl_double.setVisibility(View.GONE);

            checkAll.setVisible(true);
            match.setVisibility(View.GONE);
            invMismatch.setVisibility(View.GONE);
            mismatch.setVisibility(View.GONE);
            inGstr2a.setVisibility(View.VISIBLE);
            inPR.setVisibility(View.GONE);

        } else if (type.equalsIgnoreCase("InPR")) {
            getSupportActionBar().setTitle("In Purchase Register");
            total_invoice_text.setText("Purchase Total Invoice");
            total_taxable_text.setText("Purchase Total Taxable");
            rl_single.setVisibility(View.VISIBLE);
            rl_double.setVisibility(View.GONE);

            checkAll.setVisible(true);
            match.setVisibility(View.GONE);
            mismatch.setVisibility(View.GONE);
            invMismatch.setVisibility(View.GONE);
            inGstr2a.setVisibility(View.GONE);
            inPR.setVisibility(View.VISIBLE);

        } else if (type.equalsIgnoreCase("ALL_Records")) {
            getSupportActionBar().setTitle("All Records");
            rl_single.setVisibility(View.GONE);
            rl_double.setVisibility(View.GONE);

            checkAll.setVisible(false);
            match.setVisibility(View.GONE);
            invMismatch.setVisibility(View.GONE);
            mismatch.setVisibility(View.GONE);
            inGstr2a.setVisibility(View.GONE);
            inPR.setVisibility(View.GONE);

            marginLayoutParams.setMargins(0, 0, 0, 0);
        } else if (type.equalsIgnoreCase("RoundOff")) {
            getSupportActionBar().setTitle("Round Off");
            rl_single.setVisibility(View.GONE);
            rl_double.setVisibility(View.GONE);
        }

        data.setLayoutParams(marginLayoutParams);
        data.requestLayout();
    }

    public static void uncheckbox() {

        checkAll.setTitle("Select All");
    }

    public static void checkAll() {
        checkAll.setTitle("UnSelect All");
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        Helper.Log(TAG + " Search: ", newText);
        newText = newText.toLowerCase();
        filterCompanies(newText);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private void filterCompanies(String query) {

        tempMisMatchList.clear();
        if (query.equalsIgnoreCase("")) {
            tempMisMatchList.addAll(mismatchReportList);

        } else {

            for (int i = 0; i < mismatchReportList.size(); i++) {
                if (mismatchReportList.get(i).getCustomerName().toLowerCase().contains(query.toLowerCase()) ||
                        mismatchReportList.get(i).getGSTIN_no().toLowerCase().contains(query.toLowerCase()) ||
                        mismatchReportList.get(i).getIvNo().toLowerCase().contains(query.toLowerCase()) ||
                        mismatchReportList.get(i).getReconType().toLowerCase().contains(query.toLowerCase())) {
                    tempMisMatchList.add(mismatchReportList.get(i));
                }

            }
        }

        if (tempMisMatchList.size() == 0) {
            rvAllInvoice.setVisibility(View.GONE);
            nodate.setVisibility(View.VISIBLE);

        } else {
            rvAllInvoice.setVisibility(View.VISIBLE);
            nodate.setVisibility(View.GONE);
        }
//        mismatchAdapter = new MismatchAdapter(ReconGSTR2Activity.this, tempMisMatchList, flag, type);
//        rvAllInvoice.setAdapter(mismatchAdapter);

    }

//    private String getExcelData(ArrayList<MismatchReports> records)
//    {
//        String data ="";
//        for(int i=0;i<records.size();i++)
//        {
//            data = data + AppController.getInstance().getUserGSTIN() + "," +
//                    records.get(i).getGSTIN_no() + "," +
//                    records.get(i).getCustomerName() + "," +
//                    records.get(i).getTradeName() + "," +
//                    records.get(i).getGs_ivNo() + "," +
//                    records.get(i).getGs_financialPeriod() + "," +//Remaining
//                    records.get(i).getGs_ivDate() + "," +
//                    records.get(i).getGs_ivValue() + "," +
//                    records.get(i).getGs_ivTaxable() + "," +
//                    records.get(i).getGs_type_of_inv() + "," + //Remaining
//                    records.get(i).getGs_rev() + "," + //Remaining
//                    records.get(i).getGs_pos() + "," + //Remaining
//                    records.get(i).getGs_IGST() + "," +
//                    records.get(i).getGs_CGST() + "," +
//                    records.get(i).getGs_SGST() + "," +
//                    records.get(i).getGs_CESS() + "," +
//                    records.get(i).getGs_ivTax() + "," +
//                    records.get(i).getGs_ref_invNo() + "," +
//                    records.get(i).getGs_ref_invDate() + "," +
//                    records.get(i).getGs_cdn_reason() + "," +
//                    records.get(i).getAb_ivNo() + "," +
//                    records.get(i).getAb_financialPeriod() + "," +
//                    records.get(i).getAb_ivDate() + "," +
//                    records.get(i).getAb_ivValue() + "," +
//                    records.get(i).getAb_ivTaxable() + "," +
//                    records.get(i).getAb_type_of_inv() + "," + //Remaining
//                    records.get(i).getAb_rev() + "," + //Remaining
//                    records.get(i).getAb_pos() + "," + //Remaining
//                    records.get(i).getAb_IGST() + "," +
//                    records.get(i).getAb_CGST() + "," +
//                    records.get(i).getAb_SGST() + "," +
//                    records.get(i).getAb_CESS() + "," +
//                    records.get(i).getAb_ivTax() + "," +
//                    records.get(i).getAb_ref_invNo() + "," +
//                    records.get(i).getAb_ref_invDate() + "," +
//                    records.get(i).getAb_cdn_reason() + "," +
//                    records.get(i).getRemark() + "," +
//                    records.get(i).getMismatchColumns() + "," +
//                    records.get(i).getStatus() + "," +
//                    records.get(i).getReconType() + "," +
//                    records.get(i).getRowType() + "#" ;
//        }
//
//        return data;
//    }

    //For APK Auto Download and Install
    private boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Helper.Log(TAG, "Permission is granted");
                return true;
            } else {

                Helper.Log(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Helper.Log(TAG, "Permission is granted");
            return true;
        }
    }



    private void setActionItems(int check,boolean status)
    {
        searchView.setVisibility(check);
        filter.setVisible(false);
        checkAll.setVisible(status);
        exportExcel.setVisible(status);

    }




    private void onScrolledToBottom() {
        if (tempMisMatchList.size() < mismatchReportList.size()) {
            int x, y;
            if ((mismatchReportList.size() - tempMisMatchList.size()) >= 50) {
                x = tempMisMatchList.size();
                y = x + 50;
            } else {
                x = tempMisMatchList.size();
                y = x + mismatchReportList.size() - tempMisMatchList.size();
            }
            for (int i = x; i < y; i++) {
                tempMisMatchList.add(mismatchReportList.get(i));
            }
            mismatchAdapter.notifyDataSetChanged();
        }

    }

    protected void writeLogCat()
    {
        try
        {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder log = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null)
            {
                log.append(line);
                log.append("\n");
            }

            //Convert log to string
            final String logString = new String(log.toString());

            //Create txt file in SD Card
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() +"/" + "Log File");

            if(!dir.exists())
            {
                dir.mkdirs();
            }

            File file = new File(dir, "logcat.txt");

            //To write logcat in text file
            FileOutputStream fout = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fout);

            //Writing the string to file
            osw.write(logString);
            osw.flush();
            osw.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
