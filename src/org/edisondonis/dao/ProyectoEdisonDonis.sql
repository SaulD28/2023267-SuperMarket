drop database if exists DBSuperMarket;
 
create database DBSuperMarket;
use DBSuperMarket;

set global time_zone = '-6:00';


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


-- ---------------------Entidades con llaves foraneas -------------------------------------------------

create table Productos (
	idProducto varchar (15) not null,
	descripcionProducto varchar (45) not null,
	precioUnitario decimal (10,2) not null,
	precioDocena decimal (10,2) not null,
	precioMayor decimal (10,2) not null,
	imagenProducto varchar (15) not null,
    existencia int not null,
	idProveedor int not null,
	idTipoProducto int not null,
	primary key PK_idProducto (idProducto),
	constraint FK_Proveedores_Productos foreign key (idProveedor)
	references Proveedores (idProveedor),
	constraint FK_TipoProducto_Productos foreign key (idTipoProducto)
	references TipoProducto (idTipoProducto)
);


create table DetalleCompra (
	idDetalleCompra int not null,
	costoUnitario decimal (10,2) not null,
    cantidad int not null,
	idProducto varchar (15)not null,
	numeroDocumento int not null,
	primary key PK_idDetalleCompra (idDetalleCompra),
    constraint FK_Productos_idetalleCompra foreign key (idProducto)
    references Productos (idProducto),
    constraint FK_Compras_DetalleCompra foreign key (numeroDocumento)
    references Compras (numeroDocumento)
);

create table TelefonoProveedor (
	idTelefonoProveedor int not null,
	numeroPrincipal varchar (8) not null,
	numeroSecundario varchar (8) not null,
	observaciones varchar (45) not null,
	idProveedor int not null,
	primary key PK_idTelefonoProveedor (idTelefonoProveedor),
	constraint FK_Proveedores_TelefonoProveedor foreign key (idProveedor)
	references Proveedores (idProveedor)	
);

create table EmailProveedor (
	codigoEmailProveedor int not null,
	emailProveedor varchar (50) not null,
	descripcion varchar (100) not null,
	codigoProveedor int not null,
	primary key PK_codigoEmailProveedor (codigoEmailProveedor),
	constraint FK_Proveedores_EmailProveedor foreign key (codigoProveedor)
	references Proveedores (codigoProveedor)
);

create table Empleados (
	codigoEmpleado int not null,
	nombreEmpleado varchar (30) not null,
	apellidoEmpleado varchar (30) not null,
	sueldo decimal (10,2),
	direccion varchar (150),
	turno varchar (15),
	codigoCargoEmpleado int not null,
	primary key PK_codigoEmpleado (codigoEmpleado),
	constraint FK_CargoEmpleado_Empleados foreign key (codigoCargoEmpleado)
	references CargoEmpleado (codigoCargoEmpleado)
);


create table Factura (
	numeroFactura int not null,
    estado varchar (50) not null,
    totalFactura decimal (10,2) not null,
	fechaFactura varchar (45),
	codigoCliente int not null,
	codigoEmpleado int not null,
	primary key PK_numeroFactura (numeroFactura),
	constraint FK_Clientes_Factura foreign key (codigoCliente)
	references Clientes (codigoCliente),
	constraint FK_Empleados_Factura foreign key (codigoEmpleado)
	references Empleados (codigoEmpleado)
);

create table DetalleFactura (
	codigoDetalleFactura int not null,
    precioUnitario decimal (10,2) not null,
    cantidad int not null,
	numeroFactura int not null,
	codigoProducto varchar(15) not null,
	primary key PK_codigoDetalleFactura (codigoDetalleFactura),
	constraint FK_Factura_DetalleFactura foreign key (numeroFactura)
	references Factura (numeroFactura),
	constraint FK_Productos_DetalleFactura foreign key (codigoProducto)
	references Productos (codigoProducto)
);
 
-- ---------------------------------procedimientos almacenados -------------------------
-- ---------------------------------Clientes ---------------------------------------------
-- ---------------------------------AgregarClientes-----------------------------------------
 
Delimiter $$
	create procedure sp_AgregarClientes(in idCliente int, NitCliente varchar(10), in nombreCliente varchar(50), in apellidoCliente varchar(50),
    in direccionCliente varchar(150), in telefonoCliente varchar(8), in correoCliente varchar(45))
		begin
			insert into Clientes (idCliente, NitCliente, nombreCliente, apellidoCliente, direccionCliente,
            telefonoCliente, correoCliente) values
            (idCliente, NitCliente, nombreCliente, apellidoCliente, direccionCliente,
            telefonoCliente, correoCliente);
		End $$
Delimiter ;
 
call sp_AgregarClientes (01, '114006350', 'Harol', 'Luna', 'San Raymundo', '23002626', 'harolyLuna@gmail.com');
 
 
-- -----------------------------ListarClientes--------------------------------------------------------------------
 
Delimiter $$
	create procedure sp_ListarClientes()
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
 
call sp_ListarClientes;
 
 -- --------------------------------------BuscarClientes------------------------------------------------------
 
Delimiter $$
	create procedure sp_BuscarClientes(in id int)
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
            where idCliente = id;
		End $$
Delimiter ;
 
call sp_BuscarClientes(1);
 
 
-- ----------------------------------------------------EliminarClientes-------------------------------------------------
Delimiter $$
	create procedure sp_EliminarClientes(in id int)
		Begin
			Delete from Clientes
				where idCliente = id;
		End $$
Delimiter ;
 
call sp_EliminarClientes(1);
call sp_ListarClientes();
 
-- -------------------------------------------------------------------EditarClientes-------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarClientes(in _idCliente int, in _NitCliente varchar(10), in _nombreCliente varchar(50), in _apellidoCliente varchar(50),
			in _direccionCliente varchar(150), in _telefonoCliente varchar(8), _correoCliente varchar(45))
				Begin
					update Clientes 
						set
					idCliente = _idCliente,
					NitCliente = _NitCliente,
					nombreCliente = _nombreCliente,
					apellidoCliente = _apellidoCliente,
					direccionCliente = _direccionCliente,
					telefonoCliente = _telefonoCliente,
					correoCliente = _correoCliente
					where idCliente = _idCliente;
				end $$
Delimiter ;

-- ----------------------------------------------------------------PROCEDIMIENTOS ALMACENADOS DE PROVEEDORES----------------------------------------------------------
-- -----------------------------------------------------------------AgregarProveedores--------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_AgregarProveedores(in idProveedor int, NitProveedor varchar(10), in nombreProveedor varchar(60), in apellidoProveedor varchar(60),
    in direccionProveedor varchar(150), in razonSocial varchar(60), in contactoPrincipal varchar(100), in paginaWeb varchar(50))
		begin
			insert into Proveedores (idProveedor, NitProveedor, nombreProveedor, apellidoProveedor, direccionProveedor,
            razonSocial, contactoPrincipal, paginaWeb) values
            (idProveedor, NitProveedor, nombreProveedor, apellidoProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWeb);
		End $$
Delimiter ;

-- -----------------------------------------------------------------ListarProveedores-------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarProveedores()
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
	create procedure sp_BuscarProveedores(in id int)
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
	create procedure sp_EliminarProveedores(in id int)
		Begin
			Delete from Proveedores
				where idProveedor = id;
		End $$
Delimiter ;
 
-- -------------------------------------------------------------EditarProveedores --------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarProveedores(in _idProveedor int, _NitProveedor varchar(10), in _nombreProveedor varchar(60), in _apellidoProveedor varchar(60),
		in _direccionProveedor varchar(150), in _razonSocial varchar(60), in _contactoPrincipal varchar(100), in _paginaWeb varchar(50))
			begin
				update Proveedores P
					set
					 idProveedor = _idProveedor,
					 NitProveedor = _NitProveedor,
					 nombreProveedor = _nombreProveedor,
					 apellidoProveedor = _apellidoProveedor,
					 direccionProveedor = _direccionProveedor,
					 razonSocial = _razonSocial,
					 contactoPrincipal = _contactoPrincipal,
					 paginaWeb = _paginaWeb
                     where idProveedor = _idProveedor;
			end $$
Delimiter ;

-- --------------------------------------------------------Procedimientos Almacenados de Compras --------------------------------------------------------------------------
-- --------------------------------------------------------Agregar Compras ------------------------------------------------------------------------------------------------

Delimiter $$
	create procedure sp_AgregarCompras(in numeroDocumento int, in fechaDocumento date, in descripcion varchar(60), in totalDocumento decimal(10, 2))
		begin
			insert into Compras (numeroDocumento, fechaDocumento, descripcion, totalDocumento) values
            (numeroDocumento, fechaDocumento, descripcion, totalDocumento);
		End $$
Delimiter ;

-- -------------------------------------------------------Listar Compras----------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarCompras()
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
	create procedure sp_BuscarCompras(in id int)
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
	create procedure sp_EliminarCompras(in id int)
		Begin
			Delete from Compras
				where numeroDocumento = id;
		End $$
Delimiter ;

-- ------------------------------------------------------------Actualizar o editar Compras ------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarCompras(in _numeroDocumento int, in _fechaDocumento date, in _descripcion varchar(60), in _totalDocumento decimal(10, 2))
				Begin
					update Compras C
						set
					numeroDocumento = _numeroDocumento,
					fechaDocumento = _fechaDocumento,
					descripcion = _descripcion,
					totalDocumento = _totalDocumento
					where numeroDocumento = _numeroDocumento;
				end $$
Delimiter ;

-- --------------------------------------------------------PROCEDIMIENTOS ALMACENADOS DE CARGO EMPLEADO --------------------------------------------------------------------
-- --------------------------------------------------------Agregar CargoEmpleado -------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_AgregarCargoEmpleado(in idCargoEmpleado int, in nombreCargo varchar(50), in descripcionCargo varchar(50))
		begin
			insert into CargoEmpleado (idCargoEmpleado, nombreCargo, descripcionCargo) values
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
            from CargoEmpleado C;
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
            from CargoEmpleado C
            where idCargoEmpleado = id;
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
	create procedure sp_EditarCargoEmpleado(in _idCargoEmpleado int, in _nombreCargo varchar(50), in _descripcionCargo varchar(50))
				Begin
					update CargoEmpleado 
						set
                        idCargoEmpleado = _idCargoEmpleado,
                        nombreCargo = _nombreCargo,
                        descripcionCargo = _descripcionCargo
					where idCargoEmpleado = _idCargoEmpleado;
				end $$
Delimiter ;

-- -------------------------------------------------------------PROCEDIMIENTOS ALMACENADOS DE TIPOPRODUCTO-----------------------------------------------------------
-- -------------------------------------------------------------Agregar tipoProducto-----------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_AgregarTipoProducto(in idTipoProducto int, in descripcion varchar(50))
		begin
			insert into TipoProducto (idTipoProducto, descripcion) values
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
	create procedure sp_EditarTipoProducto(in _idTipoProducto int, in _descripcion varchar(50))
				Begin
					update TipoProducto T
						set
                        idTipoProducto = _idTipoProducto,
                        descripcion = _descripcion
					where idTipoProducto = _idTipoProducto;
				end $$
Delimiter ;

-- ************************************************************Agregar Productos******************************************************************
Delimiter $$
	create procedure sp_AgregarProductos (in idProducto varchar(15), in descripcionProducto varchar (45),in precioUnitario decimal (10,2),
    in precioDocena decimal (10,2), in precioMayor decimal (10,2), in imagenProducto varchar(15), in existencia int
    , in idProveedor int, in idTipoProducto int) 
		Begin 
			Insert into Productos (idProducto, descripcionProducto, precioUnitario, precioDocena, 
            precioMayor, imagenProducto, existencia, idProveedor, idTipoProducto) values 
            (idProducto, descripcionProducto, precioUnitario, precioDocena, 
            precioMayor, imagenProducto, existencia, idProveedor, idTipoProducto);
            
		End $$
Delimiter ;

call sp_AgregarProductos('102', 'Estas manzanas Gala orgánicas de EcoHarvest 
', 13.99, 10.99, 9.99,'', 11,1,00010);
call sp_AgregarProductos('120','El filete de res Wagyu PrimeCut 
', 15.99, 13.99, 12.99,'', 15, 2,00020);
call sp_AgregarProductos('122','El filete de res Wagyu PrimeCut 
', 12.99, 15.99, 20.99,'', 15, 2,00020);
-- *********************************************************Listar Producto***********************************************************************
-- drop procedure sp_EliminarProductos;
Delimiter $$
	create procedure sp_ListarProductos()
		Begin 
			select
            PD.idProducto,
			PD.descripcionProducto,
            PD.precioUnitario,
            PD.precioDocena,
            PD.precioMayor,
            PD.imagenProducto,
            PD.existencia,
            PD.idProveedor,
            PD.idTipoProducto	
			from Productos PD;
		End $$
Delimiter ;

call sp_ListarProductos();
 
-- *********************************************************Buscar Producto***********************************************************************
Delimiter $$
	create procedure sp_BuscarProductos(in id int)
		Begin 
			select
            PD.idProducto,
			PD.descripcionProducto,
            PD.precioUnitario,
            PD.precioDocena,
            PD.precioMayor,
            PD.imagenProducto,
            PD.existencia,
            PD.idProveedor,
            PD.idTipoProducto	
			from Productos PD
			where codigoProducto = id;
		End $$
Delimiter ;

call sp_BuscarProductos('102');

-- *********************************************************Eliminar Producto***********************************************************************
Delimiter $$
	create procedure sp_EliminarProductos(in d varchar(15))
		Begin
			Delete from Productos
				where idProducto = id;
        End $$
Delimiter ;

call sp_EliminarProductos('122');
call sp_ListarProductos();

-- ************************************************************Editar Producto*******************************************************************
Delimiter $$
	create procedure sp_EditarProductos(in _idProducto varchar(15), in _descripcionProducto varchar (100),in _precioUnitario decimal (10,2),
    in _prcioDocena decimal (10,2), in _precioMayor decimal (10,2), in _imagenProducto varchar(45), in _existencia int, in _idProveedor int, in _idTipoProducto int) 
		Begin 
			update Productos 
				set
			descripcionProducto =  _descripcionProducto,
            precioUnitario = _precioUnitario,
            precioDocena = _precioDocena ,
            precioMayor = _precioMayor,
            imagenProducto = _imagenProducto, 
            existencia = _existencia,
            idProveedor = _idProveedor,
            idTipoProducto = _idTipoProducto	
            where idProducto = _idProducto;
		End $$
Delimiter ;

-- ************************************************************Agregar DetalleCompra******************************************************************
Delimiter $$
	create procedure sp_AgregarDetalleCompra(in idDetalleCompra int, in costoUnitario decimal (10,2),in cantidad int,
	in idProducto varchar(15), in numeroDocumento int) 
		Begin 
			Insert into DetalleCompra (idDetalleCompra,costoUnitario, cantidad,
			idProducto, numeroDocumento) values 
            (idDetalleCompra,costoUnitario, cantidad,
			idProducto, numeroDocumento);
            
		End $$
Delimiter ;

call sp_AgregarDetalleCompra();
call sp_AgregarDetalleCompra();
call sp_AgregarDetalleCompra();
-- ************************************************************Listar DetalleCompra******************************************************************
Delimiter $$
	create procedure sp_ListarDetalleCompra()
		Begin 
			select
            DC.idDetalleCompra,
			DC.costoUnitario,
            DC.cantidad,
            DC.idProducto,
            DC.numeroDocumento
			from DetalleCompra DC;
		End $$
Delimiter ;

call sp_ListarDetalleCompra();
-- ************************************************************Buscar DetalleCompra******************************************************************
Delimiter $$
	create procedure sp_BuscarDetalleCompra(in id int)
		Begin 
			select
            DC.idDetalleCompra,
			DC.costoUnitario,
            DC.cantidad,
            DC.idProducto,
            DC.numeroDocumento
			from DetalleCompra DC
			where idDetalleCompra = id;
		End $$
Delimiter ;

call sp_BuscarDetalleCompra();
-- ************************************************************Eliminar DetalleCompra******************************************************************
Delimiter $$
	create procedure sp_EliminarDetalleCompra(in id int)
		Begin
			Delete from DetalleCompra
				where idDetalleCompra = id;
        End $$
Delimiter ;

call sp_EliminarDetalleCompra();
call sp_ListarDetalleCompra();
-- ************************************************************Editar DetalleCompra*********************************************************************
Delimiter $$
	create procedure sp_EditarDetalleCompra(in _idDetalleCompra varchar(15),in _costoUnitario decimal (10,2),
	in _cantidad int, in idProveedores varchar(15), in _numeroDocumento int) 
		Begin 
			update DetalleCompra 
				set
			idDetalleCompra = _idDetalleCompra,
            costoUnitario = _costoUnitario,
            cantidad = _cantidad,
            idProducto = _idProducto,
            numeroDocumento = _numeroDocumento
            where idProducto = _idProducto;
		End $$
Delimiter ;

call sp_EditarDetalleCompra(); 
call sp_ListarDetalleCompra();

-- ************************************************************Agregar TelefonoProveedor****************************************************************
Delimiter $$
	create procedure sp_AgregarTelefonoProveedor(in idTelefonoProveedor int, in numeroPrincipal varchar(8),in numeroSecundario varchar(8),
	in observaciones varchar(45), in idProveedor int) 
		Begin 
			Insert into TelefonoProveedor (idTelefonoProveedor,numeroPrincipal, numeroSecundario, observaciones, idProveedor) values 
            (idTelefonoProveedor,numeroPrincipal, numeroSecundario, observaciones, idProveedor);
            
		End $$
Delimiter ;

call sp_AgregarTelefonoProveedor();
call sp_AgregarTelefonoProveedor();
call sp_AgregarTelefonoProveedor();
-- ************************************************************Listar TelefonoProveedor****************************************************************
Delimiter $$
	create procedure sp_ListarTelefonoProveedor()
		Begin 
			select
            TP.idTelefonoProveedor,
			TP.numeroPrincipal,
            TP.numeroSecundario,
            TP.observaciones,
            TP.idProveedor
			from TelefonoProveedor TP;
		End $$
Delimiter ;

call sp_ListarTelefonoProveedor();
-- ************************************************************Buscar TelefonoProveedor****************************************************************
Delimiter $$
	create procedure sp_BuscarTelefonoProveedor(in id int)
		Begin 
			select
            TP.idTelefonoProveedor,
			TP.numeroPrincipal,
            TP.numeroSecundario,
            TP.observaciones,
            TP.idProveedor
			from TelefonoProveedor TP
			where idTelefonoProveedor = id;
		End $$
Delimiter ;

call sp_BuscarTelefonoProveedor();
-- ************************************************************Eliminar TelefonoProveedor****************************************************************
Delimiter $$
	create procedure sp_EliminarTelefonoProveedor(in id int)
		Begin
			Delete from TelefonoProveedor
				where idTelefonoProveedor = id;
        End $$
Delimiter ;

call sp_EliminarTelefonoProveedor();
call sp_ListarTelefonoProveedor();
-- ************************************************************Editar TelefonoProveedor****************************************************************
Delimiter $$
	create procedure sp_EditarTelefonoProveedor(in _idTelefonoProveedor varchar(15),in _numeroPrincipal decimal (10,2),
	in _numeroSecundario int, in _observaciones varchar(15), in _idProveedor int) 
		Begin 
			update TelefonoProveedor 
				set
            idTelefonoProveedor = _idTelefonoProveedor,
			numeroPrincipal = _numeroPrincipal,
            numeroSecundario = _numeroSecundario,
            observaciones = _observaciones,
            idProveedor = _idProveedor
            where codigoTelefonoProveedor = _idTelefonoProveedor;
		End $$
Delimiter ;
