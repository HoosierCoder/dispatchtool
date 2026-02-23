package com.hoosiercoder.dispatchtool.customer.mapper;

import com.hoosiercoder.dispatchtool.customer.dto.CustomerDTO;
import com.hoosiercoder.dispatchtool.customer.entity.Customer;
import com.hoosiercoder.dispatchtool.location.mapper.LocationMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {LocationMapper.class})
public interface CustomerMapper {
    CustomerDTO customerToCustomerDto(Customer customer);

    Customer customerDtoToCustomer(CustomerDTO customerDto);
}
