#?html <h1 id="overview">Overview</h1>
#?html <hr>
#?html <h1 id="standard-classes">Standard classes</h1>

class Object {
    #? Ancestor class for all other classes

    dict table() {
        #? Get all key=value pairs in form of a dict
    }

    object _get(string key) {
        #?badge-yellow May be null
        #? Called when field in that object is accessed through object.field or Object.get. When field is accessed through Object.getStrict or object["field"] call to this method will be omitted.
    }

    object _get_FIELDNAME() {
        #?badge-yellow May be null
        #? Called when field FIELDNAME in that object is accessed through object.FIELDNAME or Object.get.
        #? When field is accessed through Object.getStrict or object["FIELDNAME"] call to this method will be omitted.
    }

    object _set(string key, object value) {
        #?badge-yellow May be null
        #? Called when field in that object is set through object.field = value or Object.set. When field is set through Object.setStrict or object["field"] = value call to this method will be omitted.
    }

    object _set_FIELDNAME(object value) {
        #?badge-yellow May be null
        #? Called when field FIELDNAME in that object is set through object.FIELDNAME = value or Object.set.
        #? When field is accessed through Object.setStrict or object["FIELDNAME"] = value call to this method will be omitted.
    }
}