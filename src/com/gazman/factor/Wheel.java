package com.gazman.factor;

import com.gazman.math.MathUtils;

import java.math.BigInteger;

/**
 * Created by Ilya Gazman on 2/3/2016.
 */
public class Wheel {
    private long[] positions;
    private int count = 0;
    private int prime;
    private double log;
    private long[] savedPosition;
    private int savedCount;
    private MutableInteger mutableInteger[] = new MutableInteger[2];
    private MutableInteger savedMutableInteger[];
    private int powers;

    public void init(BigInteger prime, BigInteger N, BigInteger root) {
        int target = N.mod(prime).intValue();
        this.prime = prime.intValue();
        log = Math.log(this.prime);

        long[] flats = MathUtils.ressol(this.prime, target);

        int solutions = 0;
        for (int i = 0; i < flats.length; i++) {
            if (flats[i] > -1) {
                solutions++;
            }
        }

        positions = new long[solutions];
        for (int i = 0; i < flats.length; i++) {
            if (flats[i] <= -1) {
                continue;
            }
            positions[i] = flats[i] - root.mod(prime).intValue();
            if (positions[i] < 0) {
                positions[i] += this.prime;
            }
        }

        if (positions.length == 2 && positions[0] > positions[1]) {
            long tmp = positions[0];
            positions[0] = positions[1];
            positions[1] = tmp;
        }
        for (int i = 0; i < solutions; i++) {
            BigInteger bigPosition = BigInteger.valueOf(positions[i]);
            mutableInteger[i] = new MutableInteger(this.prime, root.add(bigPosition), N);
        }
    }

    public double nextLog() {
        powers = mutableInteger[count].nextPower();
        return log * powers;
    }

    public int getPowers() {
        return powers;
    }

    public long move() {
        long position = positions[count];
        positions[count] += prime;
        count = (count + 1) % positions.length;
        return position;
    }

    public boolean testMove(long limit) {
        return positions.length != 0 && positions[count] < limit;
    }

    public void savePosition() {
        savedPosition = positions.clone();
        savedMutableInteger = new MutableInteger[mutableInteger.length];
        for (int i = 0; i < mutableInteger.length; i++) {
            if(mutableInteger[i] != null){
                savedMutableInteger[i] = mutableInteger[i].clone();
            }
        }
        savedCount = count;
    }

    public void restorePosition() {
        positions = savedPosition.clone();
        mutableInteger = savedMutableInteger;
        count = savedCount;
    }

    @Override
    public String toString() {
        return prime + "";
    }

    public int getPrime() {
        return prime;
    }
}
