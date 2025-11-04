package com.example.teacoffee.myDB;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "Bill_Infor",
        primaryKeys = {"Bill_Id", "Food_Id"},
        foreignKeys = {
                @ForeignKey(
                        entity = Bill.class,
                        parentColumns = "Bill_Id",
                        childColumns = "Bill_Id",
                        onDelete = CASCADE
                ),
                @ForeignKey(
                        entity = Food.class,
                        parentColumns = "Food_Id",
                        childColumns = "Food_Id",
                        onDelete = CASCADE
                )
        },
        indices = {@Index("Bill_Id"), @Index("Food_Id")}
)
public class BillInfor {
    public int Bill_Id;
    public int Food_Id;
    public Integer Count; // số lượng
}

