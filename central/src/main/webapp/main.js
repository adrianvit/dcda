// The root URL for the RESTful services
var rootURL = "http://localhost:8080/central";

var currentNode;

// Retrieve wine list when application starts 
findAll();

$('#btnAddNode').click(function() {
	addNode($('#searchKey').val(), $('#address').val());
	return false;
});

$('#btnDeleteNode').click(function() {
	deleteNode($('#name').val());
	return false;
});

$('#nodeList a').live('click', function() {
	$('#name').val($(this).data('identity'));
	findAllProducts($(this).data('identity'));
});


$('#btnAddProduct').click(function() {
	addProduct($('#product').val());
	findAllProducts($('#nodeList a').data('identity')); //refresh the list of products
	return false;
});

$('#btnDeleteProduct').click(function() {
	deleteNode($('#productList p').data('identity'));
	return false;
});


function findAll() { // needs function in Repository.java
	console.log('findAll');
	$.ajax({
		type: 'GET',
		url: rootURL + '/rest/locate',
		dataType: "json", // data type of response
		success: renderListNodes
	});
}

//gets all products for a server
function findAllProducts(server) { // needs function in Repository.java
	console.log('findAllProducts');
	$.ajax({
		type: 'GET',
		url: rootURL + '/rest/locate/' + server,
		dataType: "json", // data type of response
		success: renderListProducts
	});
}

//function findByName(searchKey) { // not called, but maybe will be usefull
//	console.log('findByName: ' + searchKey);
//	$.ajax({
//		type: 'GET',
//		url: rootURL + '/locate/' + searchKey,
//		dataType: "json",
//		success: renderList 
//	});
//}

function addNode(name,address) {//ok
	console.log('addNode');
	$.ajax({
		type: 'POST',
		contentType: 'application/json',
		url: rootURL + '/rest/locate?' + 'name=' + name + '&' + 'adr=' + address,
		dataType: "json",
		data: formToJSON(),
		success: function(data, textStatus, jqXHR){
			$('#nodeList').append('<li id="'+name+'"><a href="#" data-identity="' + name + '">'+name+'</a></li>');
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert('addNode error: ' + textStatus);
		}
	});
}

function deleteNode(name) {//ok
	console.log('deleteNode');
	$.ajax({
		type: 'DELETE',
		url: rootURL + '/rest/locate/' + name,
		success: function(data, textStatus, jqXHR){
			$('#'+name).closest('li').remove();
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert('deleteNode error');
		}
	});
}

function addProduct(product) {//ok
	console.log('addProduct');
	var adr = '';
	$.ajax({
		type: 'GET',
		url: rootURL + '/rest/locate/' + $('#name').val(),
		dataType: "json", // data type of response
		success: function(data, textStatus, jqXHR){
			adr = data.adr;
			alert(''+adr);
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert('addProduct error: ' + textStatus);
		}
	});
	
	$.ajax({
		type: 'POST',
		contentType: 'application/json',
		url: rootURL + '/rest/locate/' + product,
		dataType: "json",
		data: formToJSON(),
		success: function(data, textStatus, jqXHR){
			alert('Product created successfully');
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert('addProduct error: ' + textStatus);
		}
	});
}

function deleteProduct(product) {//ok
	console.log('deleteNode');
	$.ajax({
		type: 'DELETE',
		url: rootURL + '/rest/locate/' + product,
		success: function(data, textStatus, jqXHR){
			alert('Product deleted successfully');
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert('deleteProduct error');
		}
	});
}

function renderListNodes(data) {
	// JAX-RS serializes an empty list as null, and a 'collection of one' as an object (not an 'array of one')
	var list = data == null ? [] : (data instanceof Array ? data : [data]);

	$('#nodeList a').remove();
	$.each(list, function(index, node) {
		$('#nodeList').append('<li id="' + node.name + '"><a href="#" data-identity="' + node.name + '">'+node.name+'</a></li>');
	});
}

function renderListProduct(data) {
	// JAX-RS serializes an empty list as null, and a 'collection of one' as an object (not an 'array of one')
	var list = data == null ? [] : (data instanceof Array ? data : [data]);

	$('#productList p').remove();
	$.each(list, function(index, product) {
		$('#productList').append('<li><a href="#" data-identity="' + product.name + '">'+product.name+'</a></li>');
	});
}

function renderDetails(node) {
	$('#name').val(node.name);
	$('#address').val(node.address);
}

// Helper function to serialize all the form fields into a JSON string
function formToJSON() {
	return JSON.stringify({
		"name": $('#name').val(), 
		"address": $('#address').val(),
		});
}
