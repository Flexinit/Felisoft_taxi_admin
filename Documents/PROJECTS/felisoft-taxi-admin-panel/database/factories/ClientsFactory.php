<?php

namespace Database\Factories;

use App\Models\Clients;
use Illuminate\Database\Eloquent\Factories\Factory;

class ClientsFactory extends Factory
{
    /**
     * The name of the factory's corresponding model.
     *
     * @var string
     */
    protected $model = Clients::class;

    /**
     * Define the model's default state.
     *
     * @return array
     */
    public function definition()
    {
        return [
            'id' => $this->faker->word,
        'first_name' => $this->faker->word,
        'last_name' => $this->faker->word,
        'email' => $this->faker->word,
        'email_verified_at' => $this->faker->date('Y-m-d H:i:s'),
        'password' => $this->faker->word,
        'phone' => $this->faker->word,
        'latitude' => $this->faker->word,
        'longitude' => $this->faker->word,
        'type' => $this->faker->word,
        'profile_picture' => $this->faker->word,
        'city' => $this->faker->word,
        'remember_token' => $this->faker->word,
        'created_at' => $this->faker->date('Y-m-d H:i:s'),
        'updated_at' => $this->faker->date('Y-m-d H:i:s'),
        'country_code' => $this->faker->randomDigitNotNull,
        'average_rating' => $this->faker->randomDigitNotNull,
        'status' => $this->faker->word,
        'activation_date' => $this->faker->word,
        'driver_type' => $this->faker->word,
        'driver_availability_status' => $this->faker->word,
        'wallet_balance' => $this->faker->word,
        'kin_first_name' => $this->faker->word,
        'kin_last_name' => $this->faker->word,
        'kin_phone' => $this->faker->word,
        'kin_email' => $this->faker->word,
        'age' => $this->faker->randomDigitNotNull,
        'date_of_birth' => $this->faker->word,
        'marital_status' => $this->faker->word,
        'address' => $this->faker->word,
        'fb_handle' => $this->faker->word,
        'twitter_handle' => $this->faker->word,
        'insta_handle' => $this->faker->word,
        'last_login' => $this->faker->word,
        'previous_charge' => $this->faker->word,
        'preference_location' => $this->faker->word,
        'preference_latitude' => $this->faker->word,
        'preference_longitude' => $this->faker->word,
        'pool_ride_preference' => $this->faker->word,
        'admin_approved' => $this->faker->word
        ];
    }
}
