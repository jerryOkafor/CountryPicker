package me.jerryhanks.countrypicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jerry Hanks on 12/14/17.
 */

class CountryCodeAdapter extends RecyclerView.Adapter<CountryCodeAdapter.CountryCodeViewHolder> implements Filterable {
    private final Context context;
    private final List<Country> countries;
    private final TextView tvNoResult;
    private List<Country> filteredCountries;
    private final OnItemClickCallback clickListener;

    public CountryCodeAdapter(Context context, @NonNull OnItemClickCallback callback, List<Country> countries,
                              RelativeLayout rlQueryHolder, SearchView searchView, TextView tvNoResult) {
        this.context = context;
        this.clickListener = callback;
        this.countries = countries;
        this.filteredCountries = countries;
        this.tvNoResult = tvNoResult;

        //attach a text change listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getFilter().filter(newText);
                return true;
            }
        });


    }

    @Override
    public CountryCodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_country_tile, parent, false);
        return new CountryCodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CountryCodeViewHolder holder, int position) {
        Country country = filteredCountries.get(position);
        holder.setCountry(country);
        holder.itemView.setOnClickListener(v -> clickListener.onItemClick(country));

    }

    @Override
    public int getItemCount() {
        return filteredCountries.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    filteredCountries = countries;
                } else {
                    ArrayList<Country> filteredList = new ArrayList<>();
                    for (Country country : countries) {
                        if (country.getCode().contains(charString) || country.getName().contains(charString)
                                || country.getCode().toLowerCase().contains(charString)
                                || country.getName().toLowerCase().contains(charString)) {
                            filteredList.add(country);
                        }
                    }
                    filteredCountries = filteredList;

                }

                FilterResults results = new FilterResults();
                results.values = filteredCountries;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredCountries = (List<Country>) results.values;
                notifyDataSetChanged();

                if (filteredCountries.isEmpty()) {
                    tvNoResult.setVisibility(View.VISIBLE);
                } else {
                    tvNoResult.setVisibility(View.GONE);
                }
            }
        };
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
