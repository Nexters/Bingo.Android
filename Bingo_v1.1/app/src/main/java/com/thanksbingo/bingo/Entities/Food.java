package com.thanksbingo.bingo.Entities;


import com.thanksbingo.db.FoodInFridgeContract;

public final class Food {

    public FoodInFridgeContract.FIFData fif;
    public boolean flagFooter;
}
// //음식 정보
//public final class Food {
//    private int imageId = 0;
//    private int count = 0;
//    private String foodName ="";
//    private String boughtDate = "";
//    private String expiryDate = "";
//     public boolean flagFooter;
//    public Food(){}
//
//    public Food(int i, String n, int c, String db, String de) {
//        this.imageId = i;
//        this.count = c;
//        this.foodName = n;
//        this.boughtDate = db;
//        this.expiryDate = de;
//    }
//
//    public Food(String foodName, String expiryDate) {
//        this.foodName = foodName;
//        this.expiryDate = expiryDate;
//    }
//
//     // EditFoodFragment에서 food의 정보를 가져오고 수정할때 사용할 생성자
//    public Food(String foodName, int count , String boughtDate, String expiryDate){
//        this.foodName = foodName;
//        this.count = count;
//        this.boughtDate = boughtDate;
//        this.expiryDate = expiryDate;
//    }
//
//
//    public void setFoodName(String n) {
//        foodName = n;
//    }
//
//    public String getFoodName(){return foodName;}
//
//    public void setExpiryDate(String d) {
//        expiryDate = d;
//    }
//
//    public String getExpiryDate(){return expiryDate;}
//
//     public int getImageId() {
//         return imageId;
//     }
//
//     public void setImageId(int imageId) {
//         this.imageId = imageId;
//     }
//
//     public int getCount() {
//         return count;
//     }
//
//     public void setCount(int count) {
//         this.count = count;
//     }
//
//     public String getBoughtDate() {
//         return boughtDate;
//     }
//
//     public void setBoughtDate(String boughtDate) {
//         this.boughtDate = boughtDate;
//     }
// }
//
