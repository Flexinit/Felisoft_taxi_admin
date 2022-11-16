<?php

namespace App\Repositories;

use App\Models\Clients;
use App\Repositories\BaseRepository;

/**
 * Class ClientsRepository
 * @package App\Repositories
 * @version November 16, 2022, 3:43 pm UTC
*/

class ClientsRepository extends BaseRepository
{
    /**
     * @var array
     */
    protected $fieldSearchable = [
        'id',
        'first_name',
        'last_name',
        'email',
        'email_verified_at',
        'password',
        'phone',
        'latitude',
        'longitude',
        'type',
        'profile_picture',
        'city',
        'remember_token',
        'country_code',
        'average_rating',
        'status',
        'activation_date',
        'driver_type',
        'driver_availability_status',
        'wallet_balance',
        'kin_first_name',
        'kin_last_name',
        'kin_phone',
        'kin_email',
        'age',
        'date_of_birth',
        'marital_status',
        'address',
        'fb_handle',
        'twitter_handle',
        'insta_handle',
        'last_login',
        'previous_charge',
        'preference_location',
        'preference_latitude',
        'preference_longitude',
        'pool_ride_preference',
        'admin_approved'
    ];

    /**
     * Return searchable fields
     *
     * @return array
     */
    public function getFieldsSearchable()
    {
        return $this->fieldSearchable;
    }

    /**
     * Configure the Model
     **/
    public function model()
    {
        return Clients::class;
    }
}
