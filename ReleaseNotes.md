##################################################
# email-3.0.7-SNAPSHOT | 04-Jan-2022
##################################################
### Code Changes:
Add request parameter 'from' to allow override from address by client


##################################################
# email-3.0.6-SNAPSHOT | 31-Dec-2021
##################################################
### Code Changes:
Add new OrderStatus enum CANCELED_BY_MERCHANT


##################################################
# email-3.0.4-SNAPSHOT | 10-October-2021
##################################################
### Code Changes:
* Changed version from 3.0.3 to 3.0.4
* added following lines in SessionRequestFilter
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	

### Properties Changes:
no changes
