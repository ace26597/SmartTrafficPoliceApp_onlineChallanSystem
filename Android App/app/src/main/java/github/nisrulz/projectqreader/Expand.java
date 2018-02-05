package github.nisrulz.projectqreader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

public class Expand extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_lay);
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("1. Not carrying valid licence while driving");
        listDataHeader.add("2. Not carrying documents as required");
        listDataHeader.add("3. Dangerous Lane cutting");
        listDataHeader.add("4. Moving against One-Way");
        listDataHeader.add("5. Overtaking dangerously");
        listDataHeader.add("6. Jumping Signal (driving at red light)");
        listDataHeader.add("7. Driving on Footpath");
        listDataHeader.add("8. Parking Violations");
        listDataHeader.add("9. Stopping at Pedestrian Crossing or Crossing Stop Line");
        listDataHeader.add("10. Horn offences");
        listDataHeader.add("11. Number Plate Offences");
        listDataHeader.add("12. Charging Excess Fare");
        listDataHeader.add("13. Misbehavior with Passenger");
        listDataHeader.add("14. Using Mobile Phones while driving");
        listDataHeader.add("15. Driving without Helmet");

        listDataHeader.add("16. Driving without valid licence");

        listDataHeader.add("17. Driving at a speed exceeding as mentioned in MVA:112");

        listDataHeader.add("18. Driving under influence of Drugs or Alcohols");

        listDataHeader.add("19. Illegal racing on road");

        listDataHeader.add("20. Using Loudspeaker beyond specified limit");

        listDataHeader.add("21. Carriage of goods which are of dangerous & hazardous nature to human life");
        listDataHeader.add("22. Overloading a vehicle beyond extent limit");
        listDataHeader.add("23. Driving of vehicle without legal authority");
        listDataHeader.add("24. Disturbance in free flow of traffic");

        listDataHeader.add("25. Allowing the vehicle to be driven by a person who does not possess a valid licence");

        // Adding child data
        List<String> r1 = new ArrayList<String>();
        r1.add("Motor Vehicle Act (MVA) : 177 ");
        r1.add("First Offence: Fine up to Rs. 100; Subsequent Offence: Fine up to Rs. 300");

        List<String> r2 = new ArrayList<String>();
        r2.add("Motor Vehicle Act (MVA) : 177 ");
        r2.add("First Offence: Fine up to Rs. 100; Subsequent Offence: Fine up to Rs. 300");

        List<String> r3 = new ArrayList<String>();
        r3.add("Motor Vehicle Act (MVA) : 177 ");
        r3.add("First Offence: Fine up to Rs. 100; Subsequent Offence: Fine up to Rs. 300");

        List<String> r4 = new ArrayList<String>();
        r4.add("Motor Vehicle Act (MVA) : 177 ");
        r4.add("First Offence: Fine up to Rs. 100; Subsequent Offence: Fine up to Rs. 300");

        List<String> r5 = new ArrayList<String>();
        r5.add("Motor Vehicle Act (MVA) : 177 ");
        r5.add("First Offence: Fine up to Rs. 100; Subsequent Offence: Fine up to Rs. 300");

        List<String> r6 = new ArrayList<String>();
        r6.add("Motor Vehicle Act (MVA) : 177 ");
        r6.add("First Offence: Fine up to Rs. 100; Subsequent Offence: Fine up to Rs. 300");

        List<String> r7 = new ArrayList<String>();
        r7.add("Motor Vehicle Act (MVA) : 177 ");
        r7.add("First Offence: Fine up to Rs. 100; Subsequent Offence: Fine up to Rs. 300");

        List<String> r8 = new ArrayList<String>();
        r8.add("Motor Vehicle Act (MVA) : 177 ");
        r8.add("First Offence: Fine up to Rs. 100; Subsequent Offence: Fine up to Rs. 300");

        List<String> r9 = new ArrayList<String>();
        r9.add("Motor Vehicle Act (MVA) : 177 ");
        r9.add("First Offence: Fine up to Rs. 100; Subsequent Offence: Fine up to Rs. 300");

        List<String> r10 = new ArrayList<String>();
        r10.add("Motor Vehicle Act (MVA) : 177 ");
        r10.add("First Offence: Fine up to Rs. 100; Subsequent Offence: Fine up to Rs. 300");

        List<String> r11 = new ArrayList<String>();
        r11.add("Motor Vehicle Act (MVA) : 177 ");
        r11.add("First Offence: Fine up to Rs. 100; Subsequent Offence: Fine up to Rs. 300");

        List<String> r12 = new ArrayList<String>();
        r12.add("Motor Vehicle Act (MVA) : 177 ");
        r12.add("First Offence: Fine up to Rs. 100; Subsequent Offence: Fine up to Rs. 300");

        List<String> r13 = new ArrayList<String>();
        r13.add("Motor Vehicle Act (MVA) : 177 ");
        r13.add("First Offence: Fine up to Rs. 100; Subsequent Offence: Fine up to Rs. 300");

        List<String> r14 = new ArrayList<String>();
        r14.add("Motor Vehicle Act (MVA) : 177 ");
        r14.add("First Offence: Fine up to Rs. 100; Subsequent Offence: Fine up to Rs. 300");

        List<String> r15 = new ArrayList<String>();
        r15.add("Motor Vehicle Act (MVA) : 177 ");
        r15.add("First Offence: Fine up to Rs. 100; Subsequent Offence: Fine up to Rs. 300");

        List<String> r16 = new ArrayList<String>();
        r16.add("Motor Vehicle Act (MVA) : 181");
        r16.add("Fine up to Rs 500 or Imprisonment up to 3 months or both.");

        List<String> r17 = new ArrayList<String>();
        r17.add("Motor Vehicle Act (MVA) : 183 ");
        r17.add("First Offence: Rs. 400; Subsequent Offence: Rs. 1000.");

        List<String> r18 = new ArrayList<String>();
        r18.add("Motor Vehicle Act (MVA) : 185");
        r18.add("First Offence: Fine up to Rs. 2000 or Imprisonment up to 6 months or both. Subsequent Offence: Fine up to Rs. 3000 or Imprisonment up to 2 yrs or both.");

        List<String> r19 = new ArrayList<String>();
        r19.add("Motor Vehicle Act (MVA) : 189 ");
        r19.add("Fine up to Rs 500 or Imprisonment up to 1 month or both.");

        List<String> r20 = new ArrayList<String>();
        r20.add("Motor Vehicle Act (MVA) :190(2) ");
        r20.add("First Offence: Rs. 1000; Subsequent Offence: Rs. 2000.");

        List<String> r21 = new ArrayList<String>();
        r21.add("Motor Vehicle Act (MVA) :190(3)");
        r21.add("First Offence: Fine up to Rs. 3000 or Imprisonment up to 1 year or both. Subsequent Offence: Fine up to Rs. 5000 or Imprisonment up to 1 years or both.");

        List<String> r22 = new ArrayList<String>();
        r22.add("Motor Vehicle Act (MVA) :194");
        r22.add("Fine up to Rs 2000 and Rs. 1000 per tonne of extra load.");


        List<String> r23 = new ArrayList<String>();
        r23.add("Motor Vehicle Act (MVA) :197");
        r23.add("Fine up to Rs 500 or Imprisonment up to 3 months or both.");


        List<String> r24 = new ArrayList<String>();
        r24.add("Motor Vehicle Act (MVA) :201");
        r24.add("Fine up to Rs 50 per hour.");


        List<String> r25 = new ArrayList<String>();
        r25.add("Motor Vehicle Act (MVA) :180");
        r25.add("Fine up to Rs 1000 or Imprisonment up to 3 months or both.");

        listDataChild.put(listDataHeader.get(0), r1); // Header, Child data
        listDataChild.put(listDataHeader.get(1), r2);
        listDataChild.put(listDataHeader.get(2), r3);
        listDataChild.put(listDataHeader.get(3), r4);
        listDataChild.put(listDataHeader.get(4), r5);
        listDataChild.put(listDataHeader.get(5), r6);
        listDataChild.put(listDataHeader.get(6), r7);
        listDataChild.put(listDataHeader.get(7), r8);
        listDataChild.put(listDataHeader.get(8), r9);
        listDataChild.put(listDataHeader.get(9), r10);
        listDataChild.put(listDataHeader.get(10), r11);
        listDataChild.put(listDataHeader.get(11), r12);
        listDataChild.put(listDataHeader.get(12), r13);
        listDataChild.put(listDataHeader.get(13), r14);
        listDataChild.put(listDataHeader.get(14), r15);
        listDataChild.put(listDataHeader.get(15), r16);
        listDataChild.put(listDataHeader.get(16), r17);
        listDataChild.put(listDataHeader.get(17), r18);
        listDataChild.put(listDataHeader.get(18), r19);
        listDataChild.put(listDataHeader.get(19), r20);
        listDataChild.put(listDataHeader.get(20), r21);
        listDataChild.put(listDataHeader.get(21), r22);
        listDataChild.put(listDataHeader.get(22), r23);
        listDataChild.put(listDataHeader.get(23), r24);
        listDataChild.put(listDataHeader.get(24), r25);
}
}