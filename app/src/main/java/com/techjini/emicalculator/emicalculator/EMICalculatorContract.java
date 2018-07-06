package com.techjini.emicalculator.emicalculator;

import android.content.Context;

public interface EMICalculatorContract {
    interface View {
        public void populateLoanType(EMICalculatorModel emiCalculatorModel);

        public void updateMonthlyEMI();

        Context getContext();

    }


    interface Presenter extends IBasePresenter<View> {
        public int calculateSeekbarPosition(double interval, String value);
        public void calculateMonthlyEMI(EMICalculatorModel emiCalculatorModel);

        public void formLoanTypeObjects(int pos);

        public boolean isValidMultiple(int min, int max, Double interval, String text);
        public boolean isInRange(int min, int max, Double interval, String text);

    }


    public interface IBasePresenter<ViewT> {

        void onViewActive(ViewT view);
    }

}
