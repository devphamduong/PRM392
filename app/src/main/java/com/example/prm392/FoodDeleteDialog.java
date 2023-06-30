package com.example.prm392;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FoodDeleteDialog extends DialogFragment {

    private static final String ARG_ITEM_NAME = "item_name";

    private String itemName;
    private DeleteConfirmationListener confirmationListener;

    public interface DeleteConfirmationListener {
        void onConfirmDelete();
    }

    public static FoodDeleteDialog newInstance(String itemName) {
        FoodDeleteDialog fragment = new FoodDeleteDialog();
        Bundle args = new Bundle();
        args.putString(ARG_ITEM_NAME, itemName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemName = getArguments().getString(ARG_ITEM_NAME);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (confirmationListener != null) {
                            confirmationListener.onConfirmDelete();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();
    }

    public void setConfirmationListener(DeleteConfirmationListener confirmationListener) {
        this.confirmationListener = confirmationListener;
    }
}