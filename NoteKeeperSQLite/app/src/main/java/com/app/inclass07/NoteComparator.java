package com.app.inclass07;

import java.util.Comparator;

/**
 * Created by Gopinath N on 2/27/2017.
 */

public class NoteComparator implements Comparator<Note> {
    public int compare(Note o1, Note o2) {
        int value1 = -1;

        if(o1.getStatus().equalsIgnoreCase(o2.getStatus())){
            if(o1.getStatus().equalsIgnoreCase("Pending")){
                value1 =0;
            }else if(o1.getStatus().equalsIgnoreCase("Completed")){
                value1 = 1;

            }
        }
        if (value1 == 0) {
            if (o1.getPriority().equalsIgnoreCase(o2.getPriority())) {
                if (o1.getPriority().equalsIgnoreCase("High")) {
                    return 0;
                } else if (o1.getPriority().equalsIgnoreCase("Medium")) {
                    return 1;
                } else if (o1.getPriority().equalsIgnoreCase("Low")) {
                    return 2;
                }else{
                    return  value1;
                }
            }else{
                return value1;
            }
                // return o1.getPriority().compareTo(o2.getPriority());
           /* if (value2 == 0) {
                return o1.building.compareTo(o2.building);
            } else {
                return value2;
            }*/
            } else if(value1 == 1){
            if (o1.getPriority().equalsIgnoreCase(o2.getPriority())) {
                if (o1.getPriority().equalsIgnoreCase("High")) {
                    return 0;
                } else if (o1.getPriority().equalsIgnoreCase("Medium")) {
                    return 1;
                } else if (o1.getPriority().equalsIgnoreCase("Low")) {
                    return 2;
                }else{
                    return  value1;
                }
            }else{
                return value1;
            }
            }
        else{
            return value1;
        }
        }

}