<?php

namespace App\Models;

use Eloquent as Model;
use Illuminate\Database\Eloquent\SoftDeletes;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;


/**
 * Class User
 * @package App\Models
 * @version November 14, 2022, 9:04 am UTC
 *
 * @property string $first_name
 * @property string $last_name
 * @property string $email
 * @property string|\Carbon\Carbon $email_verified_at
 * @property string $password
 * @property string $phone
 * @property string $latitude
 * @property string $longitude
 * @property boolean $type
 * @property string $profile_picture
 * @property string $city
 * @property string $remember_token
 * @property integer $country_code
 * @property number $average_rating
 * @property boolean $status
 * @property string $activation_date
 * @property boolean $driver_type
 * @property boolean $driver_availability_status
 * @property number $wallet_balance
 * @property string $kin_first_name
 * @property string $kin_last_name
 * @property string $kin_phone
 * @property string $kin_email
 * @property integer $age
 * @property string $date_of_birth
 * @property boolean $marital_status
 * @property string $address
 * @property string $fb_handle
 * @property string $twitter_handle
 * @property string $insta_handle
 * @property string $last_login
 * @property number $previous_charge
 * @property string $preference_location
 * @property string $preference_latitude
 * @property string $preference_longitude
 * @property boolean $pool_ride_preference
 * @property boolean $admin_approved
 */
class User extends Authenticatable
{
    use SoftDeletes;

    use HasFactory;

    public $table = 'users';

    const CREATED_AT = 'created_at';
    const UPDATED_AT = 'updated_at';


    protected $dates = ['deleted_at'];



    public $fillable = [
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
     * The attributes that should be casted to native types.
     *
     * @var array
     */
    protected $casts = [
        'id' => 'integer',
        'first_name' => 'string',
        'last_name' => 'string',
        'email' => 'string',
        'email_verified_at' => 'datetime',
        'password' => 'string',
        'phone' => 'string',
        'latitude' => 'string',
        'longitude' => 'string',
        'type' => 'boolean',
        'profile_picture' => 'string',
        'city' => 'string',
        'remember_token' => 'string',
        'country_code' => 'integer',
        'average_rating' => 'float',
        'status' => 'boolean',
        'activation_date' => 'string',
        'driver_type' => 'boolean',
        'driver_availability_status' => 'boolean',
        'wallet_balance' => 'decimal:2',
        'kin_first_name' => 'string',
        'kin_last_name' => 'string',
        'kin_phone' => 'string',
        'kin_email' => 'string',
        'age' => 'integer',
        'date_of_birth' => 'string',
        'marital_status' => 'boolean',
        'address' => 'string',
        'fb_handle' => 'string',
        'twitter_handle' => 'string',
        'insta_handle' => 'string',
        'last_login' => 'string',
        'previous_charge' => 'decimal:2',
        'preference_location' => 'string',
        'preference_latitude' => 'string',
        'preference_longitude' => 'string',
        'pool_ride_preference' => 'boolean',
        'admin_approved' => 'boolean'
    ];

    /**
     * Validation rules
     *
     * @var array
     */
    public static $rules = [
        'first_name' => 'required|string|max:191',
        'last_name' => 'required|string|max:191',
        'email' => 'required|string|max:191',
        'email_verified_at' => 'nullable',
        'password' => 'required|string|max:191',
        'phone' => 'required|string|max:191',
        'latitude' => 'required|string|max:191',
        'longitude' => 'required|string|max:191',
        'type' => 'required|boolean',
        'profile_picture' => 'nullable|string|max:191',
        'city' => 'nullable|string|max:191',
        'remember_token' => 'nullable|string|max:100',
        'created_at' => 'nullable',
        'updated_at' => 'nullable',
        'country_code' => 'required|integer',
        'average_rating' => 'nullable|numeric',
        'status' => 'required|boolean',
        'activation_date' => 'nullable|string|max:191',
        'driver_type' => 'required|boolean',
        'driver_availability_status' => 'required|boolean',
        'wallet_balance' => 'required|numeric',
        'kin_first_name' => 'required|string|max:191',
        'kin_last_name' => 'required|string|max:191',
        'kin_phone' => 'required|string|max:191',
        'kin_email' => 'required|string|max:191',
        'age' => 'nullable|integer',
        'date_of_birth' => 'nullable|string|max:191',
        'marital_status' => 'nullable|boolean',
        'address' => 'nullable|string|max:191',
        'fb_handle' => 'nullable|string|max:191',
        'twitter_handle' => 'nullable|string|max:191',
        'insta_handle' => 'nullable|string|max:191',
        'last_login' => 'nullable|string|max:191',
        'previous_charge' => 'required|numeric',
        'preference_location' => 'nullable|string|max:191',
        'preference_latitude' => 'nullable|string|max:191',
        'preference_longitude' => 'nullable|string|max:191',
        'pool_ride_preference' => 'required|boolean',
        'admin_approved' => 'required|boolean'
    ];

    public function setPasswordAttribute($password)
    {
    $this->attributes['password'] = bcrypt($password);
    }
}
