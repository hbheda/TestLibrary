package warehousedelivery.taaxgenie.in.largejsonfileparser;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import warehousedelivery.taaxgenie.in.largejsonfileparser.Helper.Helper;

import static warehousedelivery.taaxgenie.in.largejsonfileparser.ReconGSTR2Activity.checkAll;
import static warehousedelivery.taaxgenie.in.largejsonfileparser.ReconGSTR2Activity.countReason;
import static warehousedelivery.taaxgenie.in.largejsonfileparser.ReconGSTR2Activity.mismatchReportList;
import static warehousedelivery.taaxgenie.in.largejsonfileparser.ReconGSTR2Activity.months_full;
import static warehousedelivery.taaxgenie.in.largejsonfileparser.ReconGSTR2Activity.uncheckbox;

/*
  Created by Harshit on 09-08-2017.
 */

public class MismatchAdapterOld extends RecyclerView.Adapter <MismatchAdapterOld.ViewHolder> {

    private static final String TAG =MismatchAdapterOld.class.getSimpleName() ;
    private Context context;
    private Activity activity;
    private LayoutInflater inflater;
    private List<MismatchReports> mismatchListItem;
    private Helper customerHelper;
    String gstrType;
    String[] unionListCode = {"35","04","26","25","07","31","34"};
    private Boolean flag;
    private String type;
    private int item=0;
    public  ProgressBar dialog;
    ArrayList<String> selectedname  = new ArrayList<>();
    public MismatchAdapterOld(Context context, List<MismatchReports> mismatchReportsList, Boolean flags, String type, ProgressBar dialog) {
        this.context = context;
        this.mismatchListItem = mismatchReportsList;
        this.activity = (Activity) context;
        this.customerHelper = new Helper(activity);
        this.flag= flags;
        this.type=type;
        this.dialog=dialog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        View view = null;
        if (inflater != null) {
            view = inflater.inflate(R.layout.adapter_mismatch_row, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        dialog.setVisibility(View.VISIBLE);
        holder.ll_invoice_no.setVisibility(View.GONE);

        holder.cust_name.setText(mismatchListItem.get(position).getCustomerName());
        holder.invoice_no.setText(mismatchListItem.get(position).getIvNo());
        holder.gs_inv_financial_period.setText(mismatchListItem.get(position).getGs_financialPeriod());
        holder.ab_inv_financial_period.setText(mismatchListItem.get(position).getAb_financialPeriod());
        holder.gs_inv_no.setText(mismatchListItem.get(position).getGs_ivNo());
        holder.ab_inv_no.setText(mismatchListItem.get(position).getAb_ivNo());
        holder.invStatus.setText(mismatchListItem.get(position).getStatus());
        holder.gstinNo.setText(mismatchListItem.get(position).getGSTIN_no());

        /*String invoiceName="";
        if(mismatchListItem.get(position).getInvType().equalsIgnoreCase("B2B"))
            invoiceName = GSTR2Types[1];
        else
            invoiceName = GSTR2Types[3];
        holder.inv_type.setText(invoiceName);*/
        holder.inv_type.setText(mismatchListItem.get(position).getInvType().toUpperCase());

        holder.listcheckbox.setChecked(mismatchListItem.get(position).isSelectcheckbox());
        holder.listcheckbox.setTag(mismatchListItem.get(position).isSelectcheckbox());
        holder.listcheckbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                // add into arraylist

                mismatchListItem.get(position).setSelectcheckbox(cb.isChecked());
                for(int i=0;i<mismatchReportList.size();i++)
                {
                    if(type.equalsIgnoreCase("InGS")) {
                        if (mismatchReportList.get(i).getGSTIN_no().equalsIgnoreCase(mismatchListItem.get(position).getGSTIN_no()) &&
                                mismatchReportList.get(i).getGs_ivNo().equalsIgnoreCase(mismatchListItem.get(position).getGs_ivNo())) {
                            mismatchReportList.get(i).setSelectcheckbox(cb.isChecked());
                        }
                    }else
                    {
                        if (mismatchReportList.get(i).getGSTIN_no().equalsIgnoreCase(mismatchListItem.get(position).getGSTIN_no()) &&
                                mismatchReportList.get(i).getAb_ivNo().equalsIgnoreCase(mismatchListItem.get(position).getAb_ivNo())) {
                            mismatchReportList.get(i).setSelectcheckbox(cb.isChecked());
                        }
                    }
                }

                Helper.Log("TRUE STATS",mismatchListItem.get(position).getIvNo());
                if (!cb.isChecked()) {
                    uncheckbox();
                }else
                {
                    int count =0;
                    for(int i=0;i<mismatchListItem.size();i++)
                    {
                        if(mismatchListItem.get(i).isSelectcheckbox())
                            count++;
                    }

                    if(type.equalsIgnoreCase("Match"))
                    {
                        if(count== countReason)
                            checkAll();
                    }else
                    {
                        if(count== mismatchListItem.size())
                            checkAll();
                    }

                }

            }
        });

        //Helper.Log(TAG,String.valueOf(position));
        //Helper.Log(TAG,type);
        //Helper.Log(TAG,mismatchListItem.get(position).getReconType());

        if(type.equalsIgnoreCase("ALL_Records")) {
            holder.listcheckbox.setVisibility(View.GONE);
        }else if(type.equalsIgnoreCase("Match"))
        {
            if(mismatchListItem.get(position).getReconType().equalsIgnoreCase("") ||
                    mismatchListItem.get(position).getReconType().equalsIgnoreCase("AUTO MATCH") ||
                    mismatchListItem.get(position).getReconType().equalsIgnoreCase("ROUND OFF"))
            {
                holder.listcheckbox.setVisibility(View.GONE);
            }else
            {
                holder.listcheckbox.setVisibility(View.VISIBLE);
            }
        }
        else{
            holder.listcheckbox.setVisibility(View.VISIBLE);
        }

        if(mismatchListItem.get(position).getStatus().equalsIgnoreCase("MATCHED")) {
            if(mismatchListItem.get(position).getReconType().equalsIgnoreCase(""))
                holder.pFlag.setText("AUTO MATCH");
            else
                holder.pFlag.setText(mismatchListItem.get(position).getReconType().toUpperCase());
        }else
        {
            holder.pFlag.setText(mismatchListItem.get(position).getpFlag());
            /*if (mismatchListItem.get(position).getpFlag().equalsIgnoreCase("Accept")) {
                holder.pFlag.setTextColor(context.getResources().getColor(R.color.white));
            } else if (mismatchListItem.get(position).getpFlag().equalsIgnoreCase("Pending")) {
                holder.pFlag.setTextColor(context.getResources().getColor(R.color.orange_900));
            } else if (mismatchListItem.get(position).getpFlag().equalsIgnoreCase("Reject")) {
                holder.pFlag.setTextColor(context.getResources().getColor(R.color.red_900));
            } else {
                holder.pFlag.setTextColor(context.getResources().getColor(R.color.white));
            }*/
        }

        String sellerStateCode = mismatchListItem.get(position).getGSTIN_no().substring(0,2);
        String pos = mismatchListItem.get(position).getState();

        if(!pos.equalsIgnoreCase(""))
            holder.pos.setText(Helper.getStateName(pos));
        else
            holder.pos.setText(pos);


        boolean statusChanged = false;

        String unMatchedColor ="";
        String mismatchStatus = mismatchListItem.get(position).getStatus();

        switch (mismatchStatus)
        {
            case "MATCHED":
                holder.mismatch_type.setBackgroundColor(context.getResources().getColor(R.color.green_300));
                holder.cust_name.setBackgroundColor(context.getResources().getColor(R.color.green_300));
                holder.mismatch_data.setBackgroundColor(context.getResources().getColor(R.color.green_300));
                //holder.cv_mismatch_item.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.green_background_mismatch));
                statusChanged = false;
                break;
            case "MISMATCHED":
                holder.mismatch_type.setBackgroundColor(context.getResources().getColor(R.color.red_300));
                holder.cust_name.setBackgroundColor(context.getResources().getColor(R.color.red_300));
                holder.mismatch_data.setBackgroundColor(context.getResources().getColor(R.color.red_300));
                //holder.cv_mismatch_item.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.red_background_mismatch));
                statusChanged = false;
                break;
            case "INV_MISMAT":
                holder.ll_invoice_no.setVisibility(View.VISIBLE);
                holder.mismatch_type.setBackgroundColor(context.getResources().getColor(R.color.red_300));
                holder.cust_name.setBackgroundColor(context.getResources().getColor(R.color.red_300));
                holder.mismatch_data.setBackgroundColor(context.getResources().getColor(R.color.red_300));
                //holder.cv_mismatch_item.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.red_background_mismatch));
                statusChanged = false;
                break;
            case "UNMATCHED":
                if((mismatchListItem.get(position).getAb_ivDate().equalsIgnoreCase("null") || mismatchListItem.get(position).getAb_ivDate().equalsIgnoreCase("") ) &&
                        mismatchListItem.get(position).getAb_ivValue().equalsIgnoreCase("0.0")) {
                    unMatchedColor = "yellow";
                    holder.mismatch_type.setBackgroundColor(context.getResources().getColor(R.color.yellow_700));
                    holder.cust_name.setBackgroundColor(context.getResources().getColor(R.color.yellow_700));
                    holder.mismatch_data.setBackgroundColor(context.getResources().getColor(R.color.yellow_700));
                    //holder.cv_mismatch_item.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.yellow_background_mismatch));
                }
                else if((mismatchListItem.get(position).getGs_ivDate().equalsIgnoreCase("null") || mismatchListItem.get(position).getGs_ivDate().equalsIgnoreCase("")) &&
                        mismatchListItem.get(position).getGs_ivValue().equalsIgnoreCase("0.0")) {
                    unMatchedColor = "brown";
                    holder.mismatch_type.setBackgroundColor(context.getResources().getColor(R.color.brown_300));
                    holder.cust_name.setBackgroundColor(context.getResources().getColor(R.color.brown_300));
                    holder.mismatch_data.setBackgroundColor(context.getResources().getColor(R.color.brown_300));
                    //holder.cv_mismatch_item.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.brown_background_mismatch));
                }
                statusChanged = true;
                break;
        }

        if(pos.equalsIgnoreCase("") || pos.equalsIgnoreCase("null"))
        {
            holder.mismatch_cgst.setVisibility(View.VISIBLE);
            holder.mismatch_sgst.setVisibility(View.VISIBLE);
            holder.mismatch_igst.setVisibility(View.VISIBLE);
        }
        else if(pos.equalsIgnoreCase(sellerStateCode))
        {
            holder.mismatch_cgst.setVisibility(View.VISIBLE);
            holder.mismatch_sgst.setVisibility(View.VISIBLE);

            holder.mismatch_igst.setVisibility(View.GONE);
        }else
        {
            holder.mismatch_igst.setVisibility(View.VISIBLE);

            holder.mismatch_cgst.setVisibility(View.GONE);
            holder.mismatch_sgst.setVisibility(View.GONE);
        }

        if(statusChanged)
        {
            if(unMatchedColor.equalsIgnoreCase("yellow")){
                //Helper.Log("Mismatch", "Yellow "+mismatchListItem.get(position).getGs_ivDate());
                holder.gs_invDate.setText(mismatchListItem.get(position).getGs_ivDate());
                holder.gs_invoice_value.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getGs_ivValue()))));
                holder.gs_taxable_value.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getGs_ivTaxable()))));
                holder.gs_tax_value.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getGs_ivTax()))));
                holder.gs_IGST.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getGs_IGST()))));
                holder.gs_CGST.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getGs_CGST()))));
                holder.gs_SGST.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getGs_SGST()))));


                holder.ab_invDate.setText("");
                //holder.abInvMonth.setText("");
                holder.ab_invoice_value.setText("");
                holder.ab_taxable_value.setText("");
                holder.ab_tax_value.setText("");
                holder.ab_IGST.setText("");
                holder.ab_CGST.setText("");
                holder.ab_SGST.setText("");

                if(mismatchListItem.get(position).getGs_CESS().equalsIgnoreCase("0.0"))
                    holder.cess.setVisibility(View.GONE);
                else {
                    holder.cess.setVisibility(View.VISIBLE);
                    holder.cess1.setText(String.format("₹%s",Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getGs_CESS()))));
                    holder.cess2.setText("");
                }
            }else
            {
                //Helper.Log("Mismatch", "Brown "+mismatchListItem.get(position).getAb_ivDate());
                //invoiceHelper.splitDate(holder.abInvDate,holder.abInvMonth,mismatchListItem.get(position).getAb_ivDate());
                //convertdate(holder.ab_invDate,mismatchListItem.get(position).getAb_ivDate());
                holder.ab_invDate.setText(mismatchListItem.get(position).getAb_ivDate());
                holder.ab_invoice_value.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getAb_ivValue()))));
                holder.ab_taxable_value.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getAb_ivTaxable()))));
                holder.ab_tax_value.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getAb_ivTax()))));
                holder.ab_IGST.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getAb_IGST()))));
                holder.ab_CGST.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getAb_CGST()))));
                holder.ab_SGST.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getAb_SGST()))));

                holder.gs_invDate.setText("");
                //holder.gsInvMonth.setText("");
                holder.gs_invoice_value.setText("");
                holder.gs_taxable_value.setText("");
                holder.gs_tax_value.setText("");
                holder.gs_IGST.setText("");
                holder.gs_CGST.setText("");
                holder.gs_SGST.setText("");

                if(mismatchListItem.get(position).getAb_CESS().equalsIgnoreCase("0.0"))
                    holder.cess.setVisibility(View.GONE);
                else {
                    holder.cess.setVisibility(View.VISIBLE);
                    holder.cess1.setText("");
                    holder.cess2.setText(String.format("₹%s",Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getAb_CESS()))));
                }
            }

        }else
        {
            //invoiceHelper.splitDate(holder.gsInvDate,holder.gsInvMonth,mismatchListItem.get(position).getGs_ivDate());
            //convertdate(holder.gs_invDate,mismatchListItem.get(position).getGs_ivDate());
            if(type.equalsIgnoreCase("ALL_Records"))
            {
                holder.gs_invDate.setTextColor(context.getResources().getColor(R.color.black));
                holder.ab_invDate.setTextColor(context.getResources().getColor(R.color.black));
                holder.gs_invoice_value.setTextColor(context.getResources().getColor(R.color.black));
                holder.ab_invoice_value.setTextColor(context.getResources().getColor(R.color.black));
                holder.gs_taxable_value.setTextColor(context.getResources().getColor(R.color.black));
                holder.ab_taxable_value.setTextColor(context.getResources().getColor(R.color.black));
                holder.gs_tax_value.setTextColor(context.getResources().getColor(R.color.black));
                holder.ab_tax_value.setTextColor(context.getResources().getColor(R.color.black));
                holder.gs_IGST.setTextColor(context.getResources().getColor(R.color.black));
                holder.ab_IGST.setTextColor(context.getResources().getColor(R.color.black));
                holder.gs_CGST.setTextColor(context.getResources().getColor(R.color.black));
                holder.ab_CGST.setTextColor(context.getResources().getColor(R.color.black));
                holder.gs_SGST.setTextColor(context.getResources().getColor(R.color.black));
                holder.ab_SGST.setTextColor(context.getResources().getColor(R.color.black));
                holder.cess1.setTextColor(context.getResources().getColor(R.color.black));
                holder.cess2.setTextColor(context.getResources().getColor(R.color.black));
            }
            else {
                if (!mismatchListItem.get(position).getGs_ivNo().equalsIgnoreCase(mismatchListItem.get(position).getAb_ivNo())) {
                    holder.gs_inv_no.setTextColor(context.getResources().getColor(R.color.red));
                    holder.ab_inv_no.setTextColor(context.getResources().getColor(R.color.red));
                } else {
                    holder.gs_inv_no.setTextColor(context.getResources().getColor(R.color.black));
                    holder.ab_inv_no.setTextColor(context.getResources().getColor(R.color.black));
                }
                if (!mismatchListItem.get(position).getGs_ivDate().equalsIgnoreCase(mismatchListItem.get(position).getAb_ivDate())) {
                    holder.gs_invDate.setTextColor(context.getResources().getColor(R.color.red));
                    holder.ab_invDate.setTextColor(context.getResources().getColor(R.color.red));
                } else {
                    holder.gs_invDate.setTextColor(context.getResources().getColor(R.color.black));
                    holder.ab_invDate.setTextColor(context.getResources().getColor(R.color.black));
                }
                if (!mismatchListItem.get(position).getGs_ivValue().equalsIgnoreCase(mismatchListItem.get(position).getAb_ivValue())) {
                    holder.gs_invoice_value.setTextColor(context.getResources().getColor(R.color.red));
                    holder.ab_invoice_value.setTextColor(context.getResources().getColor(R.color.red));
                } else {
                    holder.gs_invoice_value.setTextColor(context.getResources().getColor(R.color.black));
                    holder.ab_invoice_value.setTextColor(context.getResources().getColor(R.color.black));
                }
                if (!mismatchListItem.get(position).getGs_ivTaxable().equalsIgnoreCase(mismatchListItem.get(position).getAb_ivTaxable())) {
                    holder.gs_taxable_value.setTextColor(context.getResources().getColor(R.color.red));
                    holder.ab_taxable_value.setTextColor(context.getResources().getColor(R.color.red));
                } else {
                    holder.gs_taxable_value.setTextColor(context.getResources().getColor(R.color.black));
                    holder.ab_taxable_value.setTextColor(context.getResources().getColor(R.color.black));
                }
                if (!mismatchListItem.get(position).getGs_ivTax().equalsIgnoreCase(mismatchListItem.get(position).getAb_ivTax())) {
                    holder.gs_tax_value.setTextColor(context.getResources().getColor(R.color.red));
                    holder.ab_tax_value.setTextColor(context.getResources().getColor(R.color.red));
                } else {
                    holder.gs_tax_value.setTextColor(context.getResources().getColor(R.color.black));
                    holder.ab_tax_value.setTextColor(context.getResources().getColor(R.color.black));
                }
                if (!mismatchListItem.get(position).getGs_IGST().equalsIgnoreCase(mismatchListItem.get(position).getAb_IGST())) {
                    holder.gs_IGST.setTextColor(context.getResources().getColor(R.color.red));
                    holder.ab_IGST.setTextColor(context.getResources().getColor(R.color.red));
                } else {
                    holder.gs_IGST.setTextColor(context.getResources().getColor(R.color.black));
                    holder.ab_IGST.setTextColor(context.getResources().getColor(R.color.black));
                }
                if (!mismatchListItem.get(position).getGs_CGST().equalsIgnoreCase(mismatchListItem.get(position).getAb_CGST())) {
                    holder.gs_CGST.setTextColor(context.getResources().getColor(R.color.red));
                    holder.ab_CGST.setTextColor(context.getResources().getColor(R.color.red));
                } else {
                    holder.gs_CGST.setTextColor(context.getResources().getColor(R.color.black));
                    holder.ab_CGST.setTextColor(context.getResources().getColor(R.color.black));
                }
                if (!mismatchListItem.get(position).getGs_SGST().equalsIgnoreCase(mismatchListItem.get(position).getAb_SGST())) {
                    holder.gs_SGST.setTextColor(context.getResources().getColor(R.color.red));
                    holder.ab_SGST.setTextColor(context.getResources().getColor(R.color.red));
                } else {
                    holder.gs_SGST.setTextColor(context.getResources().getColor(R.color.black));
                    holder.ab_SGST.setTextColor(context.getResources().getColor(R.color.black));
                }
                if (!mismatchListItem.get(position).getGs_CESS().equalsIgnoreCase(mismatchListItem.get(position).getAb_CESS())) {
                    holder.cess1.setTextColor(context.getResources().getColor(R.color.red));
                    holder.cess2.setTextColor(context.getResources().getColor(R.color.red));
                } else {
                    holder.cess1.setTextColor(context.getResources().getColor(R.color.black));
                    holder.cess2.setTextColor(context.getResources().getColor(R.color.black));
                }
            }
            holder.gs_invDate.setText(mismatchListItem.get(position).getGs_ivDate());
            holder.gs_invoice_value.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getGs_ivValue()))));
            holder.gs_taxable_value.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getGs_ivTaxable()))));
            holder.gs_tax_value.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getGs_ivTax()))));
            holder.gs_IGST.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getGs_IGST()))));
            holder.gs_CGST.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getGs_CGST()))));
            holder.gs_SGST.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getGs_SGST()))));

            //invoiceHelper.splitDate(holder.abInvDate,holder.abInvMonth,mismatchListItem.get(position).getAb_ivDate());
            //convertdate(holder.ab_invDate,mismatchListItem.get(position).getAb_ivDate());
            holder.ab_invDate.setText(mismatchListItem.get(position).getAb_ivDate());
            holder.ab_invoice_value.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getAb_ivValue()))));
            holder.ab_taxable_value.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getAb_ivTaxable()))));
            holder.ab_tax_value.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getAb_ivTax()))));
            holder.ab_IGST.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getAb_IGST()))));
            holder.ab_CGST.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getAb_CGST()))));
            holder.ab_SGST.setText(String.format("₹%s", Helper.format2DecAmountDouble(Double.parseDouble(mismatchListItem.get(position).getAb_SGST()))));

            if(mismatchListItem.get(position).getGs_CESS().equalsIgnoreCase("0.0")&& mismatchListItem.get(position).getAb_CESS().equalsIgnoreCase("0.0"))
                holder.cess.setVisibility(View.GONE);
            else {
                holder.cess.setVisibility(View.VISIBLE);
                holder.cess1.setText(String.format("₹%s",mismatchListItem.get(position).getGs_CESS()));
                holder.cess2.setText(String.format("₹%s",mismatchListItem.get(position).getAb_CESS()));
            }
        }

        /*holder.cv_mismatch_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ReconciliationItemWiseDetails.class);
                i.putExtra("Invno",mismatchListItem.get(position).getIvNo());
                i.putExtra("sellergstin",mismatchListItem.get(position).getGSTIN_no());
                i.putExtra("State",mismatchListItem.get(position).getState());

                i.putExtra("g2AInvoiceNo",mismatchListItem.get(position).getGs_ivNo());
                i.putExtra("g2aInvoiceType",mismatchListItem.get(position).getGs_type_of_inv());
                i.putExtra("prInvoiceType",mismatchListItem.get(position).getAb_type_of_inv());
                i.putExtra("g2aReturnmonth",mismatchListItem.get(position).getGs_financialPeriod());
                i.putExtra("prReturnmonth",mismatchListItem.get(position).getAb_financialPeriod());

                activity.startActivity(i);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mismatchListItem.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.listcheckbox)                    CheckBox listcheckbox;

        @BindView(R.id.ll_invoice_financial_period)     LinearLayout ll_invoice_financial_period;
        @BindView(R.id.ll_invoice_no)                   LinearLayout ll_invoice_no;

        @BindView(R.id.mismatLayot)                     LinearLayout mismatchLayot;
        @BindView(R.id.mismatch_type)                   LinearLayout mismatch_type;
        @BindView(R.id.mismatch_data)                   LinearLayout mismatch_data;
        @BindView(R.id.mismatch_date)                   LinearLayout mismatch_date;
        @BindView(R.id.mismatch_value)                  LinearLayout mismatch_value;
        @BindView(R.id.mismatch_igst)                   LinearLayout mismatch_igst;
        @BindView(R.id.mismatch_cgst)                   LinearLayout mismatch_cgst;
        @BindView(R.id.mismatch_sgst)                   LinearLayout mismatch_sgst;
        @BindView(R.id.cess)                            LinearLayout cess;

        @BindView(R.id.cust_name)                       TextView cust_name;
        @BindView(R.id.invStatus)                       TextView invStatus;
        @BindView(R.id.invoice_no)                      TextView invoice_no;
        @BindView(R.id.inv_type)                        TextView inv_type;
        @BindView(R.id.gstinNo)                         TextView gstinNo;
        @BindView(R.id.pos)                             TextView pos;

        @BindView(R.id.gs_inv_financial_period)         TextView gs_inv_financial_period;
        @BindView(R.id.gs_inv_no)                       TextView gs_inv_no;
        @BindView(R.id.gs_invDate)                      TextView gs_invDate;
        @BindView(R.id.gs_invoice_value)                TextView gs_invoice_value;
        @BindView(R.id.gs_taxable_value)                TextView gs_taxable_value;
        @BindView(R.id.gs_tax_value)                    TextView gs_tax_value;
        @BindView(R.id.gs_IGST)                         TextView gs_IGST;
        @BindView(R.id.gs_CGST)                         TextView gs_CGST;
        @BindView(R.id.gs_SGST)                         TextView gs_SGST;
        @BindView(R.id.cess1)                           TextView cess1;

        @BindView(R.id.ab_inv_financial_period)         TextView ab_inv_financial_period;
        @BindView(R.id.ab_inv_no)                       TextView ab_inv_no;
        @BindView(R.id.ab_invDate)                      TextView ab_invDate;
        @BindView(R.id.ab_invoice_value)                TextView ab_invoice_value;
        @BindView(R.id.ab_taxable_value)                TextView ab_taxable_value;
        @BindView(R.id.ab_tax_value)                    TextView ab_tax_value;
        @BindView(R.id.ab_IGST)                         TextView ab_IGST;
        @BindView(R.id.ab_CGST)                         TextView ab_CGST;
        @BindView(R.id.ab_SGST)                         TextView ab_SGST;
        @BindView(R.id.cess2)                           TextView cess2;

        @BindView(R.id.pflag)                           TextView pFlag;


        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }

    public void convertdate(TextView setDate, String invDate)
    {
        String [] splitDate = new String[3];
        if (!invDate.equalsIgnoreCase("")) {
            splitDate = invDate.split("-");
        }
        setDate.setText(splitDate[0]);
        int mon = Integer.parseInt(splitDate[1]);
        //setMonth.setText(months_all[mon-1]);
        String date = splitDate[0];
        if(date.substring(1).equalsIgnoreCase("1"))
            date = date+"st";
        else if(date.substring(1).equalsIgnoreCase("2"))
            date = date+"nd";
        else if(date.substring(1).equalsIgnoreCase("3"))
            date = date+"rd";
        else
            date = date+"th";

        setDate.setText(String.format("%s %s", date, months_full[mon - 1]));
    }
}
