##################################################
# email-3.0.10-SNAPSHOT | 24-March-2022
##################################################
### Code Changes:
For email  /no-reply/user-account, logo will load based on domain passed in


##################################################
# email-3.0.9-SNAPSHOT | 24-Feb-2022
##################################################
### Code Changes:
Set Symplified logo path to https://symplified.biz/store-assets/symplified-logo-small.png 


##################################################
# email-3.0.8-SNAPSHOT | 24-Feb-2022
##################################################
### Code Changes:
New template for reset-password


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
