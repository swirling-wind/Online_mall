function search() {
    var q = $('#search-input').val();
    if (q && q != '') {
        window.location.href = '/search?keyword=' + q;
    }
}