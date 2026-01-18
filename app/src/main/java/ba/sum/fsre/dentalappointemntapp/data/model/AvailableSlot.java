package ba.sum.fsre.dentalappointemntapp.data.model;

import com.google.gson.annotations.SerializedName;

public class AvailableSlot {
    @SerializedName("slot_time")
    public String slotTime;

    public String getSlotTime() {
        return slotTime;
    }
}
