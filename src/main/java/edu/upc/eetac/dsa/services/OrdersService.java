package edu.upc.eetac.dsa.services;

import edu.upc.eetac.dsa.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Api(value="/orders", description = "Endpoint to Order Service")
@Path("/orders")
public class OrdersService {

    final static Logger log = Logger.getLogger(OrdersService.class.getName());

    private ProductManager pm;

    public OrdersService() {
        this.pm = ProductManagerImpl.getInstance();
        if(pm.size()==0){
            Producto producto1 = new Producto("Manzana",1.5);
            Producto producto2 = new Producto("Pastel",10);
            Producto producto3 = new Producto("Cerveza",2.5);
            Producto producto4 = new Producto("Calamares",5);
            pm.addProducto(producto1);
            pm.addProducto(producto2);
            pm.addProducto(producto3);
            pm.addProducto(producto4);
            pm.addUser("Pepe");
        }
    }

    @GET
    @ApiOperation(value = "get all products in the list", notes = "x")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Producto.class, responseContainer = "List of Products")
    })
    @Path("/products")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts() {
        List<Producto> productos  = this.pm.allProducts();

        GenericEntity<List<Producto>> entity = new GenericEntity<List<Producto>>(productos){};
        return Response.status(201).entity(entity).build();
    }

    @GET
    @ApiOperation(value = "get all users in the list", notes = "x")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Usuario.class, responseContainer = "List of Users")
    })
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        HashMap<String, Usuario> users  = this.pm.allUsers();

        GenericEntity<HashMap<String, Usuario>> entity = new GenericEntity<HashMap<String, Usuario>>(users){};
        return Response.status(201).entity(entity).build();
    }

    @GET
    @ApiOperation(value = "get all orders of a user", notes = "x")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Pedido.class, responseContainer = "List of Orders"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @Path("/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrders(@PathParam("user") String user) {
        List<Pedido> pedidos;
        try {
            pedidos = this.pm.getAllOrdersOfAUser(user);
            log.info("Pedido: " +pedidos);
            GenericEntity<List<Pedido>> entity = new GenericEntity<List<Pedido>>(pedidos){};
            return Response.status(201).entity(entity).build();
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }
    }

    @GET
    @ApiOperation(value = "get all products sorted by price", notes = "x")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Producto.class, responseContainer = "List of Products")
    })
    @Path("/sortedbyprice")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsSortedByPrice() {
        List<Producto> productos  = this.pm.getAllProductsSortedByPrice();

        GenericEntity<List<Producto>> entity = new GenericEntity<List<Producto>>(productos){};
        return Response.status(201).entity(entity).build();
    }

    @GET
    @ApiOperation(value = "get all products sorted by number of sales", notes = "x")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Producto.class, responseContainer = "List of Products")
    })
    @Path("/sortedbysales")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrders() {
        List<Producto> productos  = this.pm.getAllProductsSortedByNumberOfSales();

        GenericEntity<List<Producto>> entity = new GenericEntity<List<Producto>>(productos){};
        return Response.status(201).entity(entity).build();
    }

    @POST
    @ApiOperation(value = "place an Order", notes = "x")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @Path("/placeanorder")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response placeAnOrder(Pedido p) throws ProductNotFoundException {

        String userName = p.getUser().getUsername();

        try {
            this.pm.placeAnOrder(userName, p);
            return Response.status(201).build();
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }
    }

    @DELETE
    @ApiOperation(value = "serve an Order", notes = "x")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Pedido.class, responseContainer = "Pedido")
    })
    @Path("/serveanorder")
    @Produces(MediaType.APPLICATION_JSON)
    public Response serveAnOrder(){
        Pedido pedido = this.pm.serveAnOrder();

        return Response.status(201).entity(pedido).build();
    }

    @POST
    @ApiOperation(value = "add Product", notes = "x")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful")
    })
    @Path("/addproduct")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addProduct(Producto p){
        pm.addProducto(p);

        return Response.status(201).build();
    }

    @POST
    @ApiOperation(value = "add User", notes = "x")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful")
    })
    @Path("/adduser/{user}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addProduct(@PathParam("user") String u){
        pm.addUser(u);

        return Response.status(201).build();
    }

}
