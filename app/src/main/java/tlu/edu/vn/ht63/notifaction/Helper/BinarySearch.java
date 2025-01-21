package tlu.edu.vn.ht63.notifaction.Helper;

import java.util.List;

public class BinarySearch {
    public static <T extends Comparable<T>> boolean search(List<T> list, T target){
        int left = 0;
        int right = list.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            T midElement = list.get(mid);

            if (midElement.compareTo(target) == 0) {
                return true;
            }

            if (midElement.compareTo(target) > 0) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return false;
    }
}
