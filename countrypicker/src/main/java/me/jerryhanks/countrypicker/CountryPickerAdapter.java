package me.jerryhanks.countrypicker;

import android.content.Context;
import android.support.annotation.NonNull;
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

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Jerry Hanks on 12/14/17.
 */

class CountryPickerAdapter extends SectionedRecyclerViewAdapter<CountryPickerAdapter.CountryCodeViewHolder>
        implements Filterable, SectionTitleProvider {
    private final Context context;
    private final Map<String, List<Country>> countryGroup;
    private final TextView tvNoResult;
    private final boolean showCountryCode;
    private final List<Country> countries;
    private SearchView searchView;
    private Map<String, List<Country>> filteredCountryGroup;
    private final OnItemClickCallback clickListener;

    public CountryPickerAdapter(Context context, @NonNull OnItemClickCallback callback, List<Country> countries,
                                Map<String, List<Country>> countryGroups, SearchView searchView, TextView tvNoResult,
                                boolean showCountryCodeInList) {
        this.context = context;
        this.clickListener = callback;
        this.countries = countries;
        this.countryGroup = countryGroups;
        this.searchView = searchView;
        this.filteredCountryGroup = countryGroups;
        this.tvNoResult = tvNoResult;
        this.showCountryCode = showCountryCodeInList;

        //attach a text change listener
        addSearchViewListener();


    }

    @Override
    public CountryCodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                layoutRes = R.layout.item;
                break;
            case VIEW_TYPE_HEADER:
                layoutRes = R.layout.header;
                break;
            default:
                layoutRes = R.layout.item;
                break;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new CountryCodeViewHolder(view);
    }

    @Override
    public int getSectionCount() {
        return filteredCountryGroup.size();
    }

    @Override
    public int getItemCount(int section) {
        return getItemsForSection(section).size();
    }

    private List<Country> getItemsForSection(int section) {
        return filteredCountryGroup.get(String.valueOf(Util.AZ_STRING.charAt(section)));
    }

    @Override
    public void onBindHeaderViewHolder(CountryCodeViewHolder holder, int section, boolean expanded) {
        ((TextView) holder.itemView.findViewById(R.id.tvHeader))
                .setText(String.valueOf(Util.AZ_STRING.charAt(section)).toUpperCase());

    }

    @Override
    public void onBindFooterViewHolder(CountryCodeViewHolder holder, int section) {
        //do nothing
    }

    @Override
    public void onBindViewHolder(CountryCodeViewHolder holder, int section, int relativePosition, int absolutePosition) {
        Country country = getItemsForSection(section).get(relativePosition);
        holder.setCountry(country, absolutePosition);
        holder.itemView.setOnClickListener(v -> clickListener.onItemClick(country));

    }


    //    @Override
//    public int getItemCount() {
//        return filteredCountryGroup.size();
//    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    filteredCountryGroup = countryGroup;
                } else {
                    ArrayList<Country> filteredList = new ArrayList<>();
                    for (Country country : countries) {
                        if (country.getName().toLowerCase().startsWith(charString)) {
                            //check for country code
                            if (showCountryCode && country.getCode().toLowerCase().startsWith(charString)) {
                                filteredList.add(country);
                                continue;
                            }
                            filteredList.add(country);
                        } else if (showCountryCode && country.getCode().toLowerCase().startsWith(charString)) {
                            filteredList.add(country);

                        }
                    }
                    filteredCountryGroup = Util.mapList(filteredList);

                }

                FilterResults results = new FilterResults();
                results.values = filteredCountryGroup;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredCountryGroup = (Map<String, List<Country>>) results.values;
                notifyDataSetChanged();

                if (filteredCountryGroup.isEmpty()) {
                    tvNoResult.setVisibility(View.VISIBLE);
                } else {
                    tvNoResult.setVisibility(View.GONE);
                }
            }
        };
    }

    @Override
    public String getSectionTitle(int position) {
        List<Country> c = new ArrayList<>();
        for (List<Country> countryList : filteredCountryGroup.values()) {
            c.addAll(countryList);
        }
        Country country = c.get(position);
//        if (preferredCountriesCount > position) {
//            return "★";
//        } else
        if (country != null) {
            return country.getName().substring(0, 1);
        } else {
            return "☺"; //this should never be the case
        }
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

    class CountryCodeViewHolder extends SectionedViewHolder {
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
