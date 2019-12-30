package extention

/**
 * リスト拡張関数クラス
 */
class ArrayExtension {

    /**
     * null埋めした、リストを初期化する
     * @param size リストの大きさ
     * @param T? 型引数、null許容
     * @return null埋めした、sizeのリスト
     */
    fun <T> MutableList<T?>.initWithSize(size: Int): MutableList<T?> {

        for (i in 0..size) {
            this.add(null)
        }

        return this
    }
}