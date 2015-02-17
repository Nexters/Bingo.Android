/*
 * Copyright 2015 Rudson Lima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.liveo.navigationliveo;

import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.List;

import br.liveo.adapter.NavigationLiveoItemAdapter;

public class NavigationLiveoList {

    //tak 파라미터에 List<Integer> listtogglebutton 추가
    public static List<NavigationLiveoItemAdapter> getNavigationAdapter(List<String> listNameItem, List<Integer> listIcon,
                                                                        List<Integer> listItensHeader, SparseIntArray sparceItensCount,
                                                                        int colorSelected, boolean removeSelector) {

        List<NavigationLiveoItemAdapter> mList = new ArrayList<>();
        if (listNameItem == null || listNameItem.size() == 0) {
            throw new RuntimeException("List of null or empty names. Solution: mListNameItem = new ArrayList <> (); mListNameItem.add (position, R.string.name);");
        }

        int icon;
        int count;
       // int togglebutton; //tak
        boolean isHeader;

        for (int i = 0; i < listNameItem.size(); i++) {

            String title = listNameItem.get(i);

            NavigationLiveoItemAdapter mItemAdapter;


            isHeader = (listItensHeader != null && listItensHeader.contains(i));
            count = (sparceItensCount != null ? sparceItensCount.get(i, -1) : -1);
            icon = (listIcon != null ? listIcon.get(i) : 0);
         //   togglebutton = (listIcon != null ? listIcon.get(i) : 0); //tak


            if (isHeader && icon > 0){
                throw new RuntimeException("The value of the icon for a subHeader item should be 0");
            }

            if (!isHeader) {
                if (title == null) {
                    throw new RuntimeException("Enter the item name position " + i);
                }

                if (title.trim().equals("")) {
                    throw new RuntimeException("Enter the item name position " + i);
                }
            }else{
                if (title == null) {
                    title = "";
                }

                if (title.trim().equals("")) {
                    title = "";
                }
            }

            //tak 수정됨 파라미터에 togglebutton 추가
            mItemAdapter = new NavigationLiveoItemAdapter(title, icon, isHeader, count, colorSelected, removeSelector);
            mList.add(mItemAdapter);
        }
        return mList;
    }

}
