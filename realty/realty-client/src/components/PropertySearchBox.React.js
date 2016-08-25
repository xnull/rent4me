/**
 * Created by dionis on 24.08.16.
 */
import React, {Component, PropTypes} from 'react'

export default class PropertySearchBox extends Component {
    render() {

        return (
          <form className="property-search-form border-box"
                action="http://demo.themetrail.com/realty/property-map-vertical/">

              <div className="row">


                  <div className="col-xs-12 col-sm-4 col-md-3 form-group select">
                    <input type="text" name="location" id="location"
                           placeholder="Start typing address..." className="form-control"/>
                  </div>
                  <div className="col-xs-12 col-sm-4 col-md-3 form-group select">
                      <select name="status" id="status" className="form-control chosen-select">
                          <option value="all">Any Status</option>
                          <option value="rent">
                              For Rent
                          </option>
                          <option value="sale">
                              For Sale
                          </option>
                      </select>
                  </div>
                  <div className="col-xs-12 col-sm-4 col-md-3 form-group select">
                      <select name="type" id="type" className="form-control chosen-select">
                          <option value="all">Any Type</option>
                          <option value="apartment">
                              Apartment
                          </option>
                          <option value="house">
                              House
                          </option>
                          <option value="office">
                              Office
                          </option>
                          <option value="villa">
                              Villa
                          </option>
                      </select>
                  </div>

                  <div className="col-xs-12 col-sm-4 col-md-3 form-group">
                      <select name="minrooms" id="minrooms" className="form-control chosen-select">
                          <option value="">Min. rooms</option>
                          <option value="0">0</option>
                          <option value="1">1</option>
                          <option value="2">2</option>
                          <option value="3">3</option>
                          <option value="4">4</option>
                          <option value="5">5</option>
                          <option value="6">6</option>
                          <option value="7">7</option>
                          <option value="8">8</option>
                          <option value="9">9</option>
                          <option value="10">10</option>
                      </select>
                  </div>
                  <div className="col-xs-12 col-sm-4 col-md-3 form-group">
                      <input type="number" name="maxsize" id="maxsize" value=""
                             placeholder="Min. size" min="0" className="form-control"/>
                  </div>
                  <div className="col-xs-12 col-sm-4 col-md-3 form-group">
                      <input type="text" name="keyword" id="keyword" value=""
                             placeholder="Keyword (e.g. 'office')" className="form-control"/>
                  </div>
                  <div className="col-xs-12 col-sm-4 col-md-3 form-group">


                      <select name="pricerange" id="pricerange"
                              className="form-control chosen-select">
                          <option value="">Price</option>


                          <option value="0-49999">0 - 49999</option>


                          <option value="50000-99999">50000 - 99999</option>


                          <option value="100000-149999">100000 - 149999</option>


                          <option value="150000-199999">150000 - 199999</option>


                          <option value="200000-249999">200000 - 249999</option>


                          <option value="250000-299999">250000 - 299999</option>


                          <option value="300000-349999">300000 - 349999</option>


                          <option value="350000-399999">350000 - 399999</option>


                          <option value="400000-449999">400000 - 449999</option>


                          <option value="450000-499999">450000 - 499999</option>


                          <option value="500000-549999">500000 - 549999</option>


                          <option value="550000-599999">550000 - 599999</option>


                          <option value="600000-649999">600000 - 649999</option>


                          <option value="650000-699999">650000 - 699999</option>


                          <option value="700000-749999">700000 - 749999</option>


                          <option value="750000-799999">750000 - 799999</option>


                          <option value="800000-849999">800000 - 849999</option>


                          <option value="850000-899999">850000 - 899999</option>


                          <option value="900000-949999">900000 - 949999</option>


                          <option value="950000-999999">950000 - 999999</option>


                          <option value="1000000">1000000+</option>


                      </select>


                  </div>

                  <div className="col-xs-12 col-sm-4 col-md-3 form-group">
                      <input type="submit" value="Search"
                             className="btn btn-primary btn-block form-control"/>
                  </div>


              </div>


              <input type="hidden" name="pageid" value="1149"/>


          </form>
        )
    }
}
