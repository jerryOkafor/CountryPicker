package me.jerryhanks.countrypicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jerry Hanks on 12/14/17.
 */

class CountryPickerAdapter extends RecyclerView.Adapter<CountryPickerAdapter.CountryCodeViewHolder>
        implements Filterable, SectionTitleProvider {
    private final Context context;
    private final List<Country> countries;
    private final TextView tvNoResult;
    private final boolean showCountryCode;
    private SearchView searchView;
    private List<Country> filteredCountries;
    private final OnItemClickCallback clickListener;

    public CountryPickerAdapter(Context context, @NonNull OnItemClickCallback callback,
                                List<Country> countries, SearchView searchView, TextView tvNoResult,
                                boolean showCountryCodeInList) {
        this.context = context;
        this.clickListener = callback;
        this.countries = countries;
        this.searchView = searchView;
        this.filteredCountries = countries;
        this.tvNoResult = tvNoResult;
        this.showCountryCode = showCountryCodeInList;

        //attach a text change listener
        addSearchViewListener();


    }

    @Override
    public CountryCodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_country_tile, parent, false);
        return new CountryCodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CountryCodeViewHolder holder, int position) {
        Country country = filteredCountries.get(position);
        holder.setCountry(country, position);
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
                        if ((country.getName().toLowerCase().startsWith(charString)
                                || showCountryCode) && country.getCode().toLowerCase().startsWith(charString)) {
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

    @Override
    public String getSectionTitle(int position) {
        Country country = getItem(position);
//        if (preferredCountriesCount > position) {
//            return "★";
//        } else
        if (country != null) {
            return country.getName().substring(0, 1);
        } else {
            return "☺"; //this should never be the case
        }
    }

    private Country getItem(int position) {
        return filteredCountries.get(position);
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
        addSearchViewListener();
    }

    private void addSearchViewListener() {
        if (this.searchView != null)
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
        }

        private void setCountry(Country country, int position) {
            if (position == 0) {
                divider.setVisibility(View.GONE);
            }

            if (country != null) {
                tvName.setVisibility(View.VISIBLE);
                tvCode.setVisibility(View.VISIBLE);
                flagWrapper.setVisibility(View.VISIBLE);
                if (showCountryCode) {
                    tvName.setText(context.getString(R.string.format_country_with_code, country.getName(), country.getCode().toUpperCase()));
                } else {
                    tvName.setText(context.getString(R.string.format_country, country.getName()));
                }

                tvCode.setText(context.getString(R.string.plus_prefix, country.getDialCode()));
                ivFlag.setImageResource(Util.getFlagResID(country));
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
