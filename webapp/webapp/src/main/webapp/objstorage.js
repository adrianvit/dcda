Storage.prototype.setObj = function(key, obj) {
    return this.setItem(key, JSON.stringify(obj));
};
Storage.prototype.getObj = function(key) {
	var value = this.getItem(key);
    return value && JSON.parse(value);
};