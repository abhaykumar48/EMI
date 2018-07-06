package com.techjini.emicalculator.emicalculator;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.techjini.emicalculator.BR;

import java.io.Serializable;


public class EMICalculatorModel extends BaseObservable implements Serializable {
    private String loanType;
    private String minLoanAmount;
    private String maxLaonAmount;
    private int minLoanAmountValue;
    private int maxLaonAmountValue;
    private int loanAmountInterval;
    private int minTenure;
    private int maxTenure;
    private int tenureInterval;
    private int minROI;
    private int maxROI;
    private double ROIInterval;
    private int formulaType;
    private double monthlyEMI;
    private int selectedLoanAmount;
    private int selectedTenure;
    private double selectedROI;

    @Bindable
    public double getMonthlyEMI()
    {
        return monthlyEMI;
    }

    public void setMonthlyEMI(double monthlyEMI) {
        this.monthlyEMI = monthlyEMI;
        notifyPropertyChanged(BR.monthlyEMI);
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getMinLoanAmount() {
        return minLoanAmount;
    }

    public void setMinLoanAmount(String minLoanAmount) {
        this.minLoanAmount = minLoanAmount;
    }

    public String getMaxLaonAmount() {
        return maxLaonAmount;
    }

    public void setMaxLaonAmount(String maxLaonAmount) {
        this.maxLaonAmount = maxLaonAmount;
    }

    public int getMinLoanAmountValue() {
        return minLoanAmountValue;
    }

    public void setMinLoanAmountValue(int minLoanAmountValue) {
        this.minLoanAmountValue = minLoanAmountValue;
    }

    public int getMaxLaonAmountValue() {
        return maxLaonAmountValue;
    }

    public void setMaxLaonAmountValue(int maxLaonAmountValue) {
        this.maxLaonAmountValue = maxLaonAmountValue;
    }

    public int getLoanAmountInterval() {
        return loanAmountInterval;
    }

    public void setLoanAmountInterval(int loanAmountInterval) {
        this.loanAmountInterval = loanAmountInterval;
    }

    public int getTenureInterval() {
        return tenureInterval;
    }

    public void setTenureInterval(int tenureInterval) {
        this.tenureInterval = tenureInterval;
    }

    public int getMinTenure() {
        return minTenure;
    }

    public void setMinTenure(int minTenure) {
        this.minTenure = minTenure;
    }

    public int getMaxTenure() {
        return maxTenure;
    }

    public void setMaxTenure(int maxTenure) {
        this.maxTenure = maxTenure;
    }

    public int getMinROI() {
        return minROI;
    }

    public void setMinROI(int minROI) {
        this.minROI = minROI;
    }

    public int getMaxROI() {
        return maxROI;
    }

    public void setMaxROI(int maxROI) {
        this.maxROI = maxROI;
    }

    public double getROIInterval() {
        return ROIInterval;
    }

    public void setROIInterval(double ROIInterval) {
        this.ROIInterval = ROIInterval;
    }

    public int getFormulaType() {
        return formulaType;
    }

    public void setFormulaType(int formulaType) {
        this.formulaType = formulaType;
    }

    @Bindable
    public int getSelectedLoanAmount() {
        return selectedLoanAmount;
    }

    public void setSelectedLoanAmount(int selectedLoanAmount) {
        this.selectedLoanAmount = selectedLoanAmount;
        notifyPropertyChanged(BR.selectedLoanAmount);
    }

    @Bindable
    public int getSelectedTenure() {
        return selectedTenure;
    }

    public void setSelectedTenure(int selectedTenure) {
        this.selectedTenure = selectedTenure;
        notifyPropertyChanged(BR.selectedTenure);
    }

    @Bindable
    public double getSelectedROI() {
        return selectedROI;
    }

    public void setSelectedROI(double selectedROI) {
        this.selectedROI = selectedROI;
        notifyPropertyChanged(BR.selectedROI);
    }
}