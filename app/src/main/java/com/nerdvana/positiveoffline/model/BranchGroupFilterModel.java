package com.nerdvana.positiveoffline.model;

public class BranchGroupFilterModel {
    private int branch_group_id;
    private String branch_group_name;
    private double branch_qty;
    private int selected_qty_in_branch;
    public BranchGroupFilterModel(int branch_group_id, String branch_group_name,
                                  double branch_qty, int selected_qty_in_branch) {
        this.selected_qty_in_branch = selected_qty_in_branch;
        this.branch_group_id = branch_group_id;
        this.branch_group_name = branch_group_name;
        this.branch_qty = branch_qty;
    }

    public int getSelected_qty_in_branch() {
        return selected_qty_in_branch;
    }

    public void setSelected_qty_in_branch(int selected_qty_in_branch) {
        this.selected_qty_in_branch = selected_qty_in_branch;
    }

    public double getBranch_qty() {
        return branch_qty;
    }

    public int getBranch_group_id() {
        return branch_group_id;
    }

    public String getBranch_group_name() {
        return branch_group_name;
    }
}
