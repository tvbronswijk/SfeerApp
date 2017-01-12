package com.hueandme.service.beacon;

import com.android.annotations.Nullable;
import com.hueandme.position.Position;
import com.hueandme.position.Room;

public interface OnRoomChangedListener {

    void onRoomChanged(@Nullable Room room, @Nullable Position position);
}
