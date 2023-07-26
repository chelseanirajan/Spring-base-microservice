package edu.miu.eurakdem;


    import java.util.ArrayList;
import java.util.List;
    public class TestMy {
        public static void main(String[] args) {
            List<Integer> arrayList = new ArrayList<>();
            arrayList.add(1);
            arrayList.add(2);
            arrayList.add(3);
            arrayList.add(4);
            arrayList.add(1);
            for (Integer i : arrayList) {
                if (i == 1) {
                    arrayList.remove(i);
                }
            }
            System.out.println(arrayList);
        }
}
