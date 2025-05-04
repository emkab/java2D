package res;

public class EntityArray {
    private BaseEntity[] arr;

    public EntityArray() {
        arr = new BaseEntity[0];
    }

    /**
     * @return index of added entity.
     **/
    public int addEntity(BaseEntity e) {
        BaseEntity[] _entities = new BaseEntity[arr.length + 1];
        System.arraycopy(arr, 0, _entities, 0, arr.length);
        _entities[_entities.length - 1] = e;
        arr = _entities;
        return arr.length - 1;
    }

    /**
     * @param index index of entity to get from entities array.
     * @return entity with matching index or null;
     */
    public BaseEntity getEntity(int index) {
        if (index >= 0 && index < arr.length)
            return arr[index];
        return null;
    }


    /**
     * @return index of entity in entities array if found, otherwise returns -1;
     **/
    public int getEntityIndex(BaseEntity e) {
        for (int i = 0; i < arr.length; i++)
            if (arr[i] == e) return i;
        return -1;
    }

    public int removeEntity(int index) {
        if (arr.length == 0 || index >= arr.length) return 0;
        BaseEntity[] _arr = new BaseEntity[arr.length - 1];

        boolean found = false;
        for (int i = 0; i < arr.length - 1; i++) {
            if (i == index) {
                found = true;
                continue;
            }

            _arr[found ? i - 1 : i] = arr[i];
        }

        arr = _arr;
        return arr.length;
    }

    public BaseEntity[] getArr() {
        return arr;
    }

}
