/**
 * Software: common library
 * Module: Pair template class
 * Version: 0.1
 * Licence: GPL2
 * Owner: Kim Kristo
 * Date creation : 1.2.2015
 */
package oh3ebf.lib.common;

/**
 *
 * @author operator
 */
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
