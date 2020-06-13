package com.example.studytimer.dboperation.dbobjects;

import com.example.studytimer.dboperation.TypeOfAction;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Times")
public class Times {

    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField(dataType = DataType.JAVA_DATE, canBeNull = false)
    private Date start;

    @DatabaseField(dataType = DataType.JAVA_DATE, canBeNull = false)
    private Date stop;

    @DatabaseField(canBeNull = false)
    private TypeOfAction typeOfAction;

    public Times() {

    }

    public Times(TypeOfAction typeOfAction, Date start, Date stop) {
        this.typeOfAction = typeOfAction;
        this.start = start;
        this.stop = stop;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setStop(Date stop) {
        this.stop = stop;
    }

    public void setTypeOfAction(TypeOfAction typeOfAction) {
        this.typeOfAction = typeOfAction;
    }

    public Date getStart() {
        return start;
    }

    public Date getStop() {
        return stop;
    }

    public TypeOfAction getTypeOfAction() {
        return typeOfAction;
    }
}
