/**
 * Software: common library
 * Module: pair class
 * Version: 0.2
 * Licence: GPL2
 * Owner: Kim Kristo
 * Date creation : 16.4.2013
 *
 */
package oh3ebf.common.library.utilities;

class Pair<V, K> {

    private V v;
    private K k;

    public Pair() {
    }

    public Pair(V v, K k) {
        this.v = v;
        this.k = k;
    }

    public V getV() {
        return v;
    }

    public void setV(V v) {
        this.v = v;
    }

    public K getK() {
        return k;
    }

    public void setK(K k) {
        this.k = k;
    }

    @Override
    public String toString() {
        return "Pair [v=" + v + ", k=" + k + "]";
    }
}
