package warehousedelivery.taaxgenie.in.largejsonfileparser;

/*
  Created by Harshit on 09-08-2017.
 */

import java.io.Serializable;

public class MismatchReports implements Serializable {

    private String user_GSTIN;

    private String GSTIN_no;
    private String CustomerName;
    private String tradeName;
    private String state;
    private String ivNo;
    private String InvType;
    private String invSubType;

    private String gs_ivNo;
    private String gs_ivDate;
    private String gs_ivValue;
    private String gs_ivTaxable;
    private String gs_ivTax;
    private String gs_IGST;
    private String gs_CGST;
    private String gs_SGST;
    private String gs_CESS;
    private String gs_financialPeriod;
    private String gs_rev;
    private String gs_pos;
    private String gs_type_of_inv;
    private String gs_ref_invNo;
    private String gs_ref_invDate;
    private String gs_cdn_reason;

    private String ab_ivNo;
    private String ab_ivDate;
    private String ab_ivValue;
    private String ab_ivTaxable;
    private String ab_ivTax;
    private String ab_IGST;
    private String ab_CGST;
    private String ab_SGST;
    private String ab_CESS;
    private String ab_financialPeriod;
    private String ab_rev;
    private String ab_pos;
    private String ab_type_of_inv;
    private String ab_ref_invNo;
    private String ab_ref_invDate;
    private String ab_cdn_reason;

    private String color;
    private String status;
    private String rowType;
    private String reconType;
    private String userStatus;
    private String remark;
    private String mismatchColumns;

    private String pFlag;
    private boolean selectcheckbox;

    public  MismatchReports()
    {}

    public MismatchReports(String GSTIN_no,String CustomerName,String state,String ivNo,
                           String gs_ivDate,String gs_ivValue,String gs_ivTaxable,String gs_ivTax,String gs_IGST,String gs_CGST,String gs_SGST,String gs_CESS,
                           String ab_ivDate,String ab_ivValue,String ab_ivTaxable,String ab_ivTax,String ab_IGST,String ab_CGST,String ab_SGST,String ab_CESS,
                           String color,String status,String userStatus,String Flag,String pInvoiceType){
        this.GSTIN_no = GSTIN_no;
        this.CustomerName = CustomerName;
        this.state = state;
        this.ivNo = ivNo;

        this.gs_ivDate = gs_ivDate;
        this.gs_ivValue = gs_ivValue;
        this.gs_ivTaxable = gs_ivTaxable;
        this.gs_ivTax = gs_ivTax;
        this.gs_IGST = gs_IGST;
        this.gs_CGST = gs_CGST;
        this.gs_SGST = gs_SGST;
        this.gs_CESS = gs_CESS;

        this.ab_ivDate = ab_ivDate;
        this.ab_ivValue = ab_ivValue;
        this.ab_ivTaxable = ab_ivTaxable;
        this.ab_ivTax = ab_ivTax;
        this.ab_IGST = ab_IGST;
        this.ab_CGST = ab_CGST;
        this.ab_SGST = ab_SGST;
        this.ab_CESS = ab_CESS;

        this.color = color;
        this.status = status;
        this.userStatus = userStatus;
        this.selectcheckbox=false;
        this.pFlag=Flag;
        this.InvType=pInvoiceType;
    }

    // For Excel
    public MismatchReports(String GSTIN_no,String CustomerName,String tradeName,String state,String ivNo,
                           String gs_ivNo,String gs_financialPeriod,String gs_type_of_inv,String gs_rev, String gs_pos,
                           String gs_ivDate,String gs_ivValue,String gs_ivTaxable,String gs_ivTax,String gs_IGST,String gs_CGST,String gs_SGST,String gs_CESS,
                           String gs_ref_invNo, String gs_ref_invDate, String gs_cdn_reason,
                           String ab_ivNo,String ab_financialPeriod,String ab_type_of_inv,String ab_rev, String ab_pos,
                           String ab_ivDate,String ab_ivValue,String ab_ivTaxable,String ab_ivTax,String ab_IGST,String ab_CGST,String ab_SGST,String ab_CESS,
                           String ab_ref_invNo, String ab_ref_invDate, String ab_cdn_reason,String remark,String mismatchColumns,
                           String color,String status,String rowType, String reconType,String userStatus,String Flag,String pInvoiceType,String invSubType){
        this.GSTIN_no = GSTIN_no;
        this.CustomerName = CustomerName;
        this.tradeName = tradeName;
        this.state = state;
        this.ivNo = ivNo;

        this.gs_ivNo = gs_ivNo;
        this.gs_financialPeriod = gs_financialPeriod;
        this.gs_type_of_inv = gs_type_of_inv;
        this.gs_rev = gs_rev;
        this.gs_pos = gs_pos;
        this.gs_ivDate = gs_ivDate;
        this.gs_ivValue = gs_ivValue;
        this.gs_ivTaxable = gs_ivTaxable;
        this.gs_ivTax = gs_ivTax;
        this.gs_IGST = gs_IGST;
        this.gs_CGST = gs_CGST;
        this.gs_SGST = gs_SGST;
        this.gs_CESS = gs_CESS;
        this.gs_ref_invNo = gs_ref_invNo;
        this.gs_ref_invDate = gs_ref_invDate;
        this.gs_cdn_reason = gs_cdn_reason;

        this.ab_ivNo = ab_ivNo;
        this.ab_financialPeriod = ab_financialPeriod;
        this.ab_type_of_inv = ab_type_of_inv;
        this.ab_rev = ab_rev;
        this.ab_pos = ab_pos;
        this.ab_ivDate = ab_ivDate;
        this.ab_ivValue = ab_ivValue;
        this.ab_ivTaxable = ab_ivTaxable;
        this.ab_ivTax = ab_ivTax;
        this.ab_IGST = ab_IGST;
        this.ab_CGST = ab_CGST;
        this.ab_SGST = ab_SGST;
        this.ab_CESS = ab_CESS;
        this.ab_ref_invNo = ab_ref_invNo;
        this.ab_ref_invDate = ab_ref_invDate;
        this.ab_cdn_reason = ab_cdn_reason;

        this.remark = remark;
        this.mismatchColumns = mismatchColumns;
        this.color = color;
        this.status = status;
        this.rowType = rowType;
        this.reconType = reconType;
        this.userStatus = userStatus;
        this.selectcheckbox=false;
        this.pFlag=Flag;
        this.InvType=pInvoiceType;
        this.invSubType = invSubType;
    }

    public String getUser_GSTIN() {
        return user_GSTIN;
    }
    public void setUser_GSTIN(String user_GSTIN) {
        this.user_GSTIN = user_GSTIN;
    }
    public String getGSTIN_no() {
        return GSTIN_no;
    }
    public void setGSTIN_no(String GSTIN_no) {
        this.GSTIN_no = GSTIN_no;
    }
    public String getCustomerName() {
        return CustomerName;
    }
    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }
    public String getTradeName() {
        return tradeName;
    }
    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getIvNo() {
        return ivNo;
    }
    public void setIvNo(String ivNo) {
        this.ivNo = ivNo;
    }
    public String getInvType() {
        return InvType;
    }
    public void setInvType(String invType) {
        InvType = invType;
    }
    public String getInvSubType() {
        return invSubType;
    }
    public void setInvSubType(String invSubType) {
        this.invSubType = invSubType;
    }

    public String getGs_ivNo() {
        return gs_ivNo;
    }
    public void setGs_ivNo(String gs_ivNo) {
        this.gs_ivNo = gs_ivNo;
    }
    public String getGs_ivDate() {
        return gs_ivDate;
    }
    public void setGs_ivDate(String gs_ivDate) {
        this.gs_ivDate = gs_ivDate;
    }
    public String getGs_ivValue() {
        return gs_ivValue;
    }
    public void setGs_ivValue(String gs_ivValue) {
        this.gs_ivValue = gs_ivValue;
    }
    public String getGs_ivTaxable() {
        return gs_ivTaxable;
    }
    public void setGs_ivTaxable(String gs_ivTaxable) {
        this.gs_ivTaxable = gs_ivTaxable;
    }
    public String getGs_ivTax() {
        return gs_ivTax;
    }
    public void setGs_ivTax(String gs_ivTax) {
        this.gs_ivTax = gs_ivTax;
    }
    public String getGs_IGST() {
        return gs_IGST;
    }
    public void setGs_IGST(String gs_IGST) {
        this.gs_IGST = gs_IGST;
    }
    public String getGs_CGST() {
        return gs_CGST;
    }
    public void setGs_CGST(String gs_CGST) {
        this.gs_CGST = gs_CGST;
    }
    public String getGs_SGST() {
        return gs_SGST;
    }
    public void setGs_SGST(String gs_SGST) {
        this.gs_SGST = gs_SGST;
    }
    public String getGs_CESS() {
        return gs_CESS;
    }
    public void setGs_CESS(String gs_CESS) {
        this.gs_CESS = gs_CESS;
    }
    public String getGs_financialPeriod() {
        return gs_financialPeriod;
    }
    public void setGs_financialPeriod(String gs_financialPeriod) {
        this.gs_financialPeriod = gs_financialPeriod;
    }
    public String getGs_rev() {
        return gs_rev;
    }
    public void setGs_rev(String gs_rev) {
        this.gs_rev = gs_rev;
    }
    public String getGs_pos() {
        return gs_pos;
    }
    public void setGs_pos(String gs_pos) {
        this.gs_pos = gs_pos;
    }
    public String getGs_type_of_inv() {
        return gs_type_of_inv;
    }
    public void setGs_type_of_inv(String gs_type_of_inv) {
        this.gs_type_of_inv = gs_type_of_inv;
    }
    public String getGs_ref_invNo() {
        return gs_ref_invNo;
    }
    public void setGs_ref_invNo(String gs_ref_invNo) {
        this.gs_ref_invNo = gs_ref_invNo;
    }
    public String getGs_ref_invDate() {
        return gs_ref_invDate;
    }
    public void setGs_ref_invDate(String gs_ref_invDate) {
        this.gs_ref_invDate = gs_ref_invDate;
    }
    public String getGs_cdn_reason() {
        return gs_cdn_reason;
    }
    public void setGs_cdn_reason(String gs_cdn_reason) {
        this.gs_cdn_reason = gs_cdn_reason;
    }

    public String getAb_ivNo() {
        return ab_ivNo;
    }
    public void setAb_ivNo(String ab_ivNo) {
        this.ab_ivNo = ab_ivNo;
    }
    public String getAb_ivDate() {
        return ab_ivDate;
    }
    public void setAb_ivDate(String ab_ivDate) {
        this.ab_ivDate = ab_ivDate;
    }
    public String getAb_ivValue() {
        return ab_ivValue;
    }
    public void setAb_ivValue(String ab_ivValue) {
        this.ab_ivValue = ab_ivValue;
    }
    public String getAb_ivTaxable() {
        return ab_ivTaxable;
    }
    public void setAb_ivTaxable(String ab_ivTaxable) {
        this.ab_ivTaxable = ab_ivTaxable;
    }
    public String getAb_ivTax() {
        return ab_ivTax;
    }
    public void setAb_ivTax(String ab_ivTax) {
        this.ab_ivTax = ab_ivTax;
    }
    public String getAb_IGST() {
        return ab_IGST;
    }
    public void setAb_IGST(String ab_IGST) {
        this.ab_IGST = ab_IGST;
    }
    public String getAb_CGST() {
        return ab_CGST;
    }
    public void setAb_CGST(String ab_CGST) {
        this.ab_CGST = ab_CGST;
    }
    public String getAb_SGST() {
        return ab_SGST;
    }
    public void setAb_SGST(String ab_SGST) {
        this.ab_SGST = ab_SGST;
    }
    public String getAb_CESS() {
        return ab_CESS;
    }
    public void setAb_CESS(String ab_CESS) {
        this.ab_CESS = ab_CESS;
    }
    public String getAb_financialPeriod() {
        return ab_financialPeriod;
    }
    public void setAb_financialPeriod(String ab_financialPeriod) {
        this.ab_financialPeriod = ab_financialPeriod;
    }
    public String getAb_rev() {
        return ab_rev;
    }
    public void setAb_rev(String ab_rev) {
        this.ab_rev = ab_rev;
    }
    public String getAb_pos() {
        return ab_pos;
    }
    public void setAb_pos(String ab_pos) {
        this.ab_pos = ab_pos;
    }
    public String getAb_type_of_inv() {
        return ab_type_of_inv;
    }
    public void setAb_type_of_inv(String ab_type_of_inv) {
        this.ab_type_of_inv = ab_type_of_inv;
    }
    public String getAb_ref_invNo() {
        return ab_ref_invNo;
    }
    public void setAb_ref_invNo(String ab_ref_invNo) {
        this.ab_ref_invNo = ab_ref_invNo;
    }
    public String getAb_ref_invDate() {
        return ab_ref_invDate;
    }
    public void setAb_ref_invDate(String ab_ref_invDate) {
        this.ab_ref_invDate = ab_ref_invDate;
    }
    public String getAb_cdn_reason() {
        return ab_cdn_reason;
    }
    public void setAb_cdn_reason(String ab_cdn_reason) {
        this.ab_cdn_reason = ab_cdn_reason;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getRowType() {
        return rowType;
    }
    public void setRowType(String rowType) {
        this.rowType = rowType;
    }
    public String getReconType() {
        return reconType;
    }
    public void setReconType(String reconType) {
        this.reconType = reconType;
    }
    public String getUserStatus() {
        return userStatus;
    }
    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getMismatchColumns() {
        return mismatchColumns;
    }
    public void setMismatchColumns(String mismatchColumns) {
        this.mismatchColumns = mismatchColumns;
    }
    public String getpFlag() {
        return pFlag;
    }
    public void setpFlag(String pFlag) {
        this.pFlag = pFlag;
    }
    public boolean isSelectcheckbox() {
        return selectcheckbox;
    }
    public void setSelectcheckbox(boolean selectcheckbox) {
        this.selectcheckbox = selectcheckbox;
    }

}
