package me.jerryhanks.countrypicker;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * @author Jerry Hanks on 12/14/17.
 */

class CountryCodeAdapter extends RecyclerView.Adapter<CountryCodeAdapter.CountryCodeViewHolder> {
    private final Context context;
    private final List<Country> countries;
    private final OnItemClickCallback clickListener;

    public CountryCodeAdapter(Context context, @NonNull OnItemClickCallback callback, List<Country> countries,
                              RelativeLayout rlQueryHolder, EditText editText_search,
                              TextView textView_noResult, Dialog dialog, ImageView imgClearQuery) {
        this.context = context;
        this.clickListener = callback;
        this.countries = countries;

    }

    @Override
    public CountryCodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_country_tile, parent, false);
        return new CountryCodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CountryCodeViewHolder holder, int position) {
        Country country = countries.get(position);
        holder.setCountry(country);
        holder.itemView.setOnClickListener(v -> clickListener.onItemClick(country));

    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    class CountryCodeViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rootView;
        TextView tvName, tvCode;
        ImageView ivFlag;
        LinearLayout flagWrapper;
        View divider;

        CountryCodeViewHolder(View itemView) {
            super(itemView);
            rootView = (RelativeLayout) itemView;
            tvName = rootView.findViewById(R.id.tvName);
            tvCode = rootView.findViewById(R.id.tvCode);
            ivFlag = rootView.findViewById(R.id.ivFlag);
            flagWrapper = rootView.findViewById(R.id.flagWrapper);
            divider = rootView.findViewById(R.id.preferenceDivider);

//            if (codePicker.getDialogTextColor() != 0) {
//                tvName.setTextColor(codePicker.getDialogTextColor());
//                tvCode.setTextColor(codePicker.getDialogTextColor());
//                divider.setBackgroundColor(codePicker.getDialogTextColor());
//            }
//
//            try {
//                if (codePicker.getDialogTypeFace() != null) {
//                    if (codePicker.getDialogTypeFaceStyle() != CountryCodePicker.DEFAULT_UNSET) {
//                        tvCode.setTypeface(codePicker.getDialogTypeFace(), codePicker.getDialogTypeFaceStyle());
//                        tvName.setTypeface(codePicker.getDialogTypeFace(), codePicker.getDialogTypeFaceStyle());
//                    } else {
//                        tvCode.setTypeface(codePicker.getDialogTypeFace());
//                        tvName.setTypeface(codePicker.getDialogTypeFace());
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }

        public void setCountry(Country country) {

            if (country != null) {
//                divider.setVisibility(View.GONE);
                tvName.setVisibility(View.VISIBLE);
                tvCode.setVisibility(View.VISIBLE);

                // TODO: 12/15/17 Fix
//                if (codePicker.isCcpDialogShowPhoneCode()) {
//                    tvCode.setVisibility(View.VISIBLE);
//                } else {
//                    tvCode.setVisibility(View.GONE);
//                }

                flagWrapper.setVisibility(View.VISIBLE);
                tvName.setText(context.getString(R.string.format_country, country.getName(), country.getCode().toUpperCase()));
                tvCode.setText(context.getString(R.string.plus_prefix, country.getDialCode()));
                ivFlag.setImageResource(Country.getFlagResID(country));
            } else {
                divider.setVisibility(View.VISIBLE);
                tvName.setVisibility(View.GONE);
                tvCode.setVisibility(View.GONE);
                flagWrapper.setVisibility(View.GONE);
            }
        }

        public RelativeLayout getMainView() {
            return rootView;
        }
    }

    interface OnItemClickCallback {
        void onItemClick(Country country);
    }
}
