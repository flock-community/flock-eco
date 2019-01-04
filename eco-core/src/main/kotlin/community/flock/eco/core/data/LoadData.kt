package community.flock.eco.core.data

interface LoadData<T> {

    fun load():Iterable<T>
}