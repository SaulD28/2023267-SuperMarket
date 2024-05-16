drop database if exists DBSuperMarket;
 
create database DBSuperMarket;
use DBSuperMarket;
 
create table Clientes(
	idCliente int not null,
	NitCliente varchar(10) not null,
	nombreCliente varchar (50) not null,
	apellidoCliente varchar(50) not null,
	direccionCliente varchar(150),
	telefonoCliente varchar(8),
	correoCliente varchar(45),
	primary key PK_idCliente(idCliente)
);
 
create table Proveedores(
	idProveedor int not null,
    NitProveedor varchar(10) not null,
    nombreProveedor varchar (60),
    apellidoProveedor varchar (60),
    direccionProveedor varchar (150),
    razonSocial varchar (60),
    contactoPrincipal varchar(100),
    paginaWeb varchar(50),
    primary key PK_idProveedor(idProveedor)
);

create table Compras(
	numeroDocumento int,
    fechaDocumento date,
    descripcion varchar(60),
    totalDocumento decimal(10,2),
    primary key PK_numeroDocumento(numeroDocumento)
);

create table CargoEmpleado(
	idCargoEmpleado int,
    nombreCargo varchar(50),
    descripcionCargo varchar(50),
    primary key PK_idCargoEmpleado(idCargoEmpleado)
);

create table TipoProducto(
	idTipoProducto int,
    descripcion varchar(50),
    primary key PK_idTipoProducto(idTipoProducto)
);
 
-- ---------------------------------procedimientos almacenados -------------------------
-- ---------------------------------Clientes ---------------------------------------------
-- ---------------------------------AgregarClientes-----------------------------------------
 
Delimiter $$
	create procedure sp_AgregarCliente(in idCliente int, NitCliente varchar(10), in nombreCliente varchar(50), in apellidoCliente varchar(50),
    in direccionCliente varchar(150), in telefonoCliente varchar(8), in correoCliente varchar(45))
		begin
			insert into Clientes (idCliente, NitCliente, nombreCliente, apellidoCliente, direccionCliente,
            telefonoCliente, correoCliente) values
            (idCliente, NitCliente, nombreCliente, apellidoCliente, direccionCliente,
            telefonoCliente, correoCliente);
		End $$
Delimiter ;
 
call sp_AgregarCliente (01, '114006350', 'Harol', 'Luna', 'San Raymundo', '23002626', 'harolyLuna@gmail.com');
 
 
-- -----------------------------ListarClientes--------------------------------------------------------------------
 
Delimiter $$
	create procedure sp_ListarCliente()
		Begin
			select
            C.idCliente,
            C.NitCliente,
            C.nombreCliente,
            C.apellidoCliente,
            C.direccionCliente,
            C.telefonoCliente,
            C.correoCliente
            from Clientes C;
		End $$
Delimiter ;
 
call sp_ListarCliente;
 
 -- --------------------------------------BuscarClientes------------------------------------------------------
 
Delimiter $$
	create procedure sp_BuscarCliente(in idCliente int)
		Begin
			select
            C.idCliente,
            C.NitCliente,
            C.nombreCliente,
            C.apellidoCliente,
            C.direccionCliente,
            C.telefonoCliente,
            C.correoCliente
            from Clientes C
            where idCliente = idCliente;
		End $$
Delimiter ;
 
call sp_BuscarCliente(1)
 
 
-- ----------------------------------------------------EliminarClientes-------------------------------------------------
Delimiter $$
	create procedure sp_EliminarCliente(in id int)
		Begin
			Delete from Clientes
				where idCliente = id;
		End $$
Delimiter ;
 
call sp_EliminarCliente(1);
call sp_ListarCliente();
 
-- -------------------------------------------------------------------EditarClientes-------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarCliente(in idCliente int, in NitCliente varchar(10), in nombreCliente varchar(50), in apellidoCliente varchar(50),
			in direccionCliente varchar(150), in telefonoCliente varchar(8), correoCliente varchar(45))
				Begin
					update Clientes C
						set
					C.idCliente = idCliente,
					C.NitCliente = NitCliente,
					C.nombreCliente = nombreCliente,
					C.apellidoCliente = apellidoCliente,
					C.direccionCliente = direccionCliente,
					C.telefonoCliente = telefonoCliente,
					C.correoCliente = correoCliente
					where idCliente = idCliente;
				end $$
Delimiter ;

-- ----------------------------------------------------------------PROCEDIMIENTOS ALMACENADOS DE PROVEEDORES----------------------------------------------------------
-- -----------------------------------------------------------------AgregarProveedores--------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_AgregarProveedor(in idProveedor int, NitProveedor varchar(10), in nombreProveedor varchar(60), in apellidoProveedor varchar(60),
    in direccionProveedor varchar(150), in razonPrincipal varchar(60), in contactoPrincipal varchar(100), in paginaWeb varchar(50))
		begin
			insert into Proveedores (idProveedor, NitProveedor, nombreProveedor, apellidoProveedor, direccionProveedor,
            razonSocial, contactoPrincipal, paginaWeb) values
            (idProveedor, NitProveedor, nombreProveedor, apellidoProveedor, direccionProveedor, razonPrincipal, contactoPrincipal, paginaWeb);
		End $$
Delimiter ;

-- -----------------------------------------------------------------ListarProveedores-------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarProveedor()
		Begin
			select
            P.idProveedor,
            P.NitProveedor,
            P.nombreProveedor,
            P.apellidoProveedor,
            P.direccionProveedor,
            P.razonSocial,
            P.contactoPrincipal,
            P.paginaWeb
            from Proveedores P;
		End $$
Delimiter ;

-- ----------------------------------------------------------------BuscarProveedores----------------------------------------------------------------------------------------

Delimiter $$
	create procedure sp_BuscarProveedor(in id int)
		Begin
			select
           P.idProveedor,
            P.NitProveedor,
            P.nombreProveedor,
            P.apellidoProveedor,
            P.direccionProveedor,
            P.razonSocial,
            P.contactoPrincipal,
            P.paginaWeb
            from Proveedores P
            where idProveedor = id;
		End $$
Delimiter ;

-- --------------------------------------------------------------EliminarProveedores----------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EliminarProveedor(in id int)
		Begin
			Delete from Proveedores
				where idProveedor = id;
		End $$
Delimiter ;
 
-- -------------------------------------------------------------EditarProveedores --------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarProveedor(in idProveedor int, NitProveedor varchar(10), in nombreProveedor varchar(60), in apellidoProveedor varchar(60),
		in direccionProveedor varchar(150), in razonPrincipal varchar(60), in contactoPrincipal varchar(100), in paginaWeb varchar(50))
			begin
				update Proveedores P
					set
					 P.idProveedor = idProveedor,
					 P.NitProveedor = NitProveedor,
					 P.nombreProveedor = nombreProveedor,
					 P.apellidoProveedor = apellidoProveedor,
					 P.direccionProveedor = direccionProveedor,
					 P.razonSocial = razonSocial,
					 P.contactoPrincipal = contactoPrincipal,
					 P.paginaWeb = pagineWeb
                     where idProveedor = idProveedor;
			end $$
Delimiter ;

-- --------------------------------------------------------Procedimientos Almacenados de Compras --------------------------------------------------------------------------
-- --------------------------------------------------------Agregar Compras ------------------------------------------------------------------------------------------------

Delimiter $$
	create procedure sp_AgregarCompra(in numeroDocumento int, in fechaDocumento date, in descripcion varchar(60), in totalDocumento decimal(10, 2))
		begin
			insert into Clientes (numeroDocumento, fechaDocumento, descripcion, totalDocumento) values
            (numeroDocumento, fechaDocumento, descripcion, totalDocumento);
		End $$
Delimiter ;

-- -------------------------------------------------------Listar Compras----------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarCompra()
		Begin
			select
			C.numeroDocumento,
            C.fechaDocumento,
            C.descripcion,
            C.totalDocumento
            from Compras C;
		End $$
Delimiter ;

-- ------------------------------------------------------------- Buscar Compras --------------------------------------------------------------------------------------------

Delimiter $$
	create procedure sp_BuscarCompra(in id int)
		Begin
			select
			C.numeroDocumento,
            C.fechaDocumento,
            C.descripcion,
            C.totalDocumento
            from Compras C
            where numeroDocumento = id;
		End $$
Delimiter ;

-- -------------------------------------------------------------Eliminar Producto -----------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EliminarCompra(in id int)
		Begin
			Delete from Compras
				where numeroDocumento = id;
		End $$
Delimiter ;

-- ------------------------------------------------------------Actualizar o editar Compras ------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarCompra(in numeroDocumento int, in fechaDocumento date, in descripcion varchar(60), in totalDocumento decimal(10, 2))
				Begin
					update Compras C
						set
					C.numeroDocumento = numeroDocumento,
					C.fechaDocumento = fechaDocumento,
					C.descripcion = descripcion,
					C.totalDocumento = totalDocumento
					where idCliente = id;
				end $$
Delimiter ;

-- --------------------------------------------------------PROCEDIMIENTOS ALMACENADOS DE CARGO EMPLEADO --------------------------------------------------------------------
-- --------------------------------------------------------Agregar CargoEmpleado -------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_AgregarCargoEmpleado(in idCargoEmpleado int, in nombreCargo varchar(50), in descripcionCargo varchar(50))
		begin
			insert into Compras (idCargoEmpleado, nombreCargo, descripcionCargo) values
            (idCargoEmpleado, nombreCargo, descripcionCargo);
		End $$
Delimiter ;

-- --------------------------------------------------------Listar CargoEmpleado---------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarCargoEmpleado()
		Begin
			select
			C.idCargoEmpleado,
            C.nombreCargo,
            C.descripcionCargo
            from Compras C;
		End $$
Delimiter ;

-- --------------------------------------------------------- Buscar CargoEmpleado-----------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_BuscarCargoEmpleado(in id int)
		Begin
			select
			C.idCargoEmpleado,
            C.nombreCargo,
            C.descripcionCargo
            from Compras C
            where numeroDocumento = id;
		End $$
Delimiter ;

-- ------------------------------------------------------------Eliminar CargoEmpleado --------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EliminarCargoEmpleado(in id int)
		Begin
			Delete from CargoEmpleado
				where idCargoEmpleado = id;
		End $$
Delimiter ;

-- ------------------------------------------------------------Actualizar o Listar  CargoEmpleado ---------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarCargoEmpleado(in idCargoEmpleado int, in nombreCargo varchar(50), in descripcionCargo varchar(50))
				Begin
					update CargoEmpleado C
						set
                        C.idCargoEmpleado = idCargoEmpelado,
                        C.nombreCargo = nombreCargo,
                        C.descripcionCargo = descripcionCargo
					where idCargoEmpleado = id;
				end $$
Delimiter ;

-- -------------------------------------------------------------PROCEDIMIENTOS ALMACENADOS DE TIPOPRODUCTO-----------------------------------------------------------
-- -------------------------------------------------------------Agregar tipoProducto-----------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_AgregarTipoProducto(in idTipoProducto int, in descripcion varchar(50))
		begin
			insert into Compras (idTipoProducto, descripcion) values
            (idTipoProducto, descripcion);
		End $$
Delimiter ;

-- ----------------------------------------------------------Listar TipoProducto------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarTipoProducto()
		Begin
			select
			T.idTipoProducto,
            T.descripcion
            from TipoProducto T;
		End $$
Delimiter ;

-- ------------------------------------------------------------ Buscar TipoProducto------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_BuscarTipoProducto(in id int)
		Begin
			select
			T.idTipoProducto,
            T.descripcion
            from TipoProducto T
            where idTipoProducto = id;
		End $$
Delimiter ;

-- -----------------------------------------------------------Eliminar TipoProducto------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EliminarTipoProducto(in id int)
		Begin
			Delete from TipoProducto
				where idTipoProducto = id;
		End $$
Delimiter ;

-- ----------------------------------------------------------Actualizar o editar TipoProducto-------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarTipoProducto(in idTipoProducto int, in descripcion varchar(50))
				Begin
					update TipoProducto T
						set
                        T.idTipoProducto = idTipoProducto,
                        T.descripcion = descripcion
					where idTipoProducto = id;
				end $$
Delimiter ;