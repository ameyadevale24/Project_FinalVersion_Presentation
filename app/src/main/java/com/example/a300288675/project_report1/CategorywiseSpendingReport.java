package com.example.a300288675.project_report1;

final public class CategorywiseSpendingReport {
    float[] amount = new float[10];
    String[] categories = new String[10];

    public CategorywiseSpendingReport(float[] amount, String[] categories) {
        this.amount = amount;
        this.categories = categories;
    }

    public float[] getamount() {
        return amount;
    }

    public String[] getCategories() {
        return categories;
    }
}
