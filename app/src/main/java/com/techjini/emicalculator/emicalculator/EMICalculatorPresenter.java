package com.techjini.emicalculator.emicalculator;

import android.text.TextUtils;

import com.techjini.emicalculator.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;


public class EMICalculatorPresenter implements EMICalculatorContract.Presenter {

    private final int EMI_CALCULATOR_FLAT_FORMULA = 1;
    private final int EMI_CALCULATOR_REDUCING_FORMULA = 2;
    private EMICalculatorContract.View view;


    EMICalculatorPresenter(EMICalculatorFragment emiCalculatorFragment) {
        view = emiCalculatorFragment;
    }


    @Override
    public int calculateSeekbarPosition(double max, String value) {
        if (!TextUtils.isEmpty(value)) {
            return ((int) (max * 100 / Double.parseDouble(value)));

        } else {
            return 0;
        }
    }


    @Override
    public void calculateMonthlyEMI(EMICalculatorModel emiCalculatorModel) {
        switch (emiCalculatorModel.getFormulaType()) {
            case EMI_CALCULATOR_FLAT_FORMULA: {
                calculateFlatEMI(emiCalculatorModel);
                break;
            }
            case EMI_CALCULATOR_REDUCING_FORMULA: {
                calculateReducingEMI(emiCalculatorModel);
                break;
            }

        }

    }

    private void calculateReducingEMI(EMICalculatorModel emiCalculatorModel) {
        BigDecimal partA = new BigDecimal(emiCalculatorModel.getSelectedLoanAmount() * (emiCalculatorModel.getSelectedROI() / 100));
        BigDecimal partB = new BigDecimal(Math.pow((1 + (emiCalculatorModel.getSelectedROI() / 100)), 36));
        BigDecimal partC = new BigDecimal(12 * Math.pow((1 + (emiCalculatorModel.getROIInterval() / 100)), 36) - 1);
        double partD = (partA.doubleValue() * partB.doubleValue()) / partC.doubleValue();
        /*DecimalFormat formatter = new DecimalFormat("####.#####");
        formatter.setMaximumFractionDigits(4);
        emiCalculatorModel.setMonthlyEMI(Double.parseDouble((formatter.format((Double)partD))));*/
        emiCalculatorModel.setMonthlyEMI(partD);
        view.updateMonthlyEMI();
    }

    private void calculateFlatEMI(EMICalculatorModel emiCalculatorModel) {
        double interest = ((emiCalculatorModel.getSelectedLoanAmount()) * (emiCalculatorModel.getSelectedROI() / 100)) * (emiCalculatorModel.getSelectedTenure() / 12);
        emiCalculatorModel.setMonthlyEMI((emiCalculatorModel.getSelectedLoanAmount() + interest) / emiCalculatorModel.getSelectedTenure());
        view.updateMonthlyEMI();
    }

    @Override
    public void formLoanTypeObjects(int i) {
        final String[] minLoanAmountValue = view.getContext().getResources().getStringArray(R.array.emi_calculator_min_loan_amount);
        final String[] maxLoanAmountValue = view.getContext().getResources().getStringArray(R.array.emi_calculator_max_loan_amount_value);
        final String[] maxLoanAmount = view.getContext().getResources().getStringArray(R.array.emi_calculator_max_loan_amount);
        final String[] loanAmountInterval = view.getContext().getResources().getStringArray(R.array.emi_calculator_loan_amount_interval);
        final String[] minTenureValue = view.getContext().getResources().getStringArray(R.array.emi_calculator_min_tenure);
        final String[] maxTenureValue = view.getContext().getResources().getStringArray(R.array.emi_calculator_max_tenure);
        final String[] tenureInterval = view.getContext().getResources().getStringArray(R.array.emi_calculator_tenure_interval);
        final String[] minROIValue = view.getContext().getResources().getStringArray(R.array.emi_calculator_min_roi);
        final String[] maxROIValue = view.getContext().getResources().getStringArray(R.array.emi_calculator_max_roi);
        final String[] ROIInterval = view.getContext().getResources().getStringArray(R.array.emi_calculator_roi_interval);
        // ArrayList<EMICalculatorModel> emiCalculatorModels=new ArrayList<>();

        //  for (int i=0;i<loanTypes.length;i++){
        EMICalculatorModel emiCalculatorModel = new EMICalculatorModel();
        emiCalculatorModel.setMinLoanAmountValue(Integer.parseInt(minLoanAmountValue[i]));
        emiCalculatorModel.setMaxLaonAmountValue(Integer.parseInt(maxLoanAmountValue[i]));
        emiCalculatorModel.setMaxLaonAmount(maxLoanAmount[i]);
        emiCalculatorModel.setLoanAmountInterval(Integer.parseInt(loanAmountInterval[i]));
        emiCalculatorModel.setMinTenure(Integer.parseInt(minTenureValue[i]));
        emiCalculatorModel.setMaxTenure(Integer.parseInt(maxTenureValue[i]));
        emiCalculatorModel.setTenureInterval(Integer.parseInt(tenureInterval[i]));
        emiCalculatorModel.setMinROI(Integer.parseInt(minROIValue[i]));
        emiCalculatorModel.setMaxROI(Integer.parseInt(maxROIValue[i]));
        emiCalculatorModel.setROIInterval(Double.parseDouble(ROIInterval[i]));
        emiCalculatorModel.setSelectedROI(Double.parseDouble(minROIValue[i]));
        emiCalculatorModel.setSelectedTenure(Integer.parseInt(minTenureValue[i]));
        emiCalculatorModel.setSelectedLoanAmount(Integer.parseInt(minLoanAmountValue[i]));
        if (i < 6) {
            emiCalculatorModel.setFormulaType(EMI_CALCULATOR_FLAT_FORMULA);
        } else {
            emiCalculatorModel.setFormulaType(EMI_CALCULATOR_REDUCING_FORMULA);
        }
        // emiCalculatorModels.add(emiCalculatorModel);
        view.populateLoanType(emiCalculatorModel);
        //  }


    }

    @Override
    public boolean isValidMultiple(int min, int max, Double interval, String text) {
        if (TextUtils.isEmpty(text)) {
            return true;
        } else {

            Double value = Double.parseDouble(text);
            value = Double.parseDouble((new DecimalFormat("##.#").format(value)));
            // if (isInRange(min, max, value)) {
            if ((int) (Double.parseDouble(text) % value) == 0) {
                return true;

            }
        }
        // }
        return false;
    }

    @Override
    public boolean isInRange(int min, int max, Double interval, String text) {
        Double value = Double.parseDouble(text);
        return value >= min && value <= max;
    }


    @Override
    public void onViewActive(EMICalculatorContract.View view) {

    }
}
