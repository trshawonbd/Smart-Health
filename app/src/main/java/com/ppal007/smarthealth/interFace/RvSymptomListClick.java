package com.ppal007.smarthealth.interFace;

public interface RvSymptomListClick {
    void symptomClick(
            int position,
            String symptomTblId,
            String bpUp,
            String bpDown,
            String spo2,
            String ecg,
            String glBfr,
            String glAfr,
            String tmperature,
            String submitDate
    );
}
