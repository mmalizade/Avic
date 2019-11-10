package ir.moovic.entertainment.utils;

import com.bumptech.glide.load.Key;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

import androidx.annotation.NonNull;


public class GlideLongSignature implements Key {

    private long currentVersion;

    public GlideLongSignature(long currentVersion) {
        this.currentVersion = currentVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GlideLongSignature) {
            GlideLongSignature other = (GlideLongSignature) o;
            return (this.currentVersion == other.currentVersion);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) this.currentVersion;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest md) {
        md.update(ByteBuffer.allocate(Long.SIZE).putLong(currentVersion).array());
    }

}
